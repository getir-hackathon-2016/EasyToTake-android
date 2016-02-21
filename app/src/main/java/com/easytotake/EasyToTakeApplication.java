package com.easytotake;

import android.app.Application;

import com.easytotake.rest.AbstractCallback;
import com.easytotake.rest.RestClient;
import com.easytotake.rest.model.BasketModel;
import com.easytotake.util.PreferencesManager;

import retrofit.Call;
import retrofit.Response;


public class EasyToTakeApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RestClient.init();

        /**
         * Load cached shopping's in server-side
         */
        Call<BasketModel> call = RestClient.getService().getShoppingCardCount("1");
        call.enqueue(new AbstractCallback<BasketModel>() {
            @Override
            public void onSuccess(Response<BasketModel> response) {
                if (response.isSuccess()) {
                    PreferencesManager.getInstance(EasyToTakeApplication.this).setShopping(Integer.parseInt(response.body().getContent()));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // TODO ignore
            }
        });
    }
}
