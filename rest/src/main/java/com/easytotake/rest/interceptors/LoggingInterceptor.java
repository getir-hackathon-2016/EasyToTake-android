package com.easytotake.rest.interceptors;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import okio.Buffer;

public class LoggingInterceptor implements Interceptor {
    private final static String TAG = LoggingInterceptor.class.getName();

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if (copy.body() != null)
                copy.body().writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i(TAG, String.format("Sending request %s", request.url()));
        Log.d(TAG, String.format("Headers : %n%s", request.headers()));
        Log.d(TAG, "Body: " + bodyToString(request));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();

        Log.i(TAG, String.format("Received response  %s in in %.1fms", response.request().url(), (t2 - t1) / 1e6d));
        Log.d(TAG, String.format("Headers : %n%s", response.headers()));
        return response;
    }

}