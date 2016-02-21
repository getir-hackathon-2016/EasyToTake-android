package com.easytotake.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerViewActivity extends BaseDrawerActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    public static void startScannerActivity(Context context) {
        Intent intent = new Intent(context, ScannerViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setAutoFocus(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        String text = rawResult.getText();
        Snackbar.make(mScannerView, "goto detail page.found text:" + text, Snackbar.LENGTH_LONG).show();
//        mScannerView.resumeCameraPreview(this);
    }
}
