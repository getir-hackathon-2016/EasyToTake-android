package com.easytotake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.easytotake.BaseActivity;
import com.easytotake.R;
import com.easytotake.components.view.QrCodeScannerView;
import com.google.zxing.Result;

import butterknife.Bind;


public class ScannerActivity extends BaseDrawerActivity implements QrCodeScannerView.ResultHandler {

    @Bind(R.id.mScannerView)
    QrCodeScannerView mScannerView;

    public static void startScannerActivity(Context context) {
        Intent intent = new Intent(context, ScannerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_barcode_scanner);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(true);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        Log.v("asd", rawResult.getText());
        Log.v("asd", rawResult.getBarcodeFormat().toString());
    }
}
