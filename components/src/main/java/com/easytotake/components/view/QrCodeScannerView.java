package com.easytotake.components.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.AttributeSet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import me.dm7.barcodescanner.core.BarcodeScannerView;
import me.dm7.barcodescanner.core.DisplayUtils;


public class QrCodeScannerView extends BarcodeScannerView {

    private MultiFormatReader mMultiFormatReader;
    private ResultHandler mResultHandler;

    public QrCodeScannerView(Context context) {
        super(context);
        initMultiFormatReader();
    }

    public QrCodeScannerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initMultiFormatReader();
    }

    public void setResultHandler(ResultHandler resultHandler) {
        mResultHandler = resultHandler;
    }

    private void initMultiFormatReader() {
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singletonList(BarcodeFormat.QR_CODE));
        mMultiFormatReader = new MultiFormatReader();
        mMultiFormatReader.setHints(hints);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPreviewSize();
        int width = size.width;
        int height = size.height;

        if (DisplayUtils.getScreenOrientation(getContext()) == Configuration.ORIENTATION_PORTRAIT) {
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++)
                    rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
            int tmp = width;
            width = height;
            height = tmp;
            data = rotatedData;
        }

        Result rawResult = null;
        PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);

        if (source != null) {
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = mMultiFormatReader.decodeWithState(bitmap);
            } catch (ReaderException re) {
                // continue
            } catch (NullPointerException npe) {
                // This is terrible
            } catch (ArrayIndexOutOfBoundsException aoe) {

            } finally {
                mMultiFormatReader.reset();
            }
        }

        if (rawResult != null) {
            stopCamera();
            if (mResultHandler != null) {
                mResultHandler.handleResult(rawResult);
            }
        } else {
            camera.setOneShotPreviewCallback(this);
        }
    }

    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview(width, height);
        if (rect == null) {
            return null;
        }
        // Go ahead and assume it's YUV rather than die.
        PlanarYUVLuminanceSource source = null;

        try {
            source = new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top,
                    rect.width(), rect.height(), false);
        } catch (Exception e) {
        }

        return source;
    }

    public interface ResultHandler {
        void handleResult(Result rawResult);
    }
}
