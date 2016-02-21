package com.easytotake.listener;

import com.google.zxing.Result;


public interface QrCodeReadListener {
    void OnSuccess(Result result);
}
