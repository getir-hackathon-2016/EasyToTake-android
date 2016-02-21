package com.easytotake.rest;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * AbstractCallback is an abstract class to handle unauthorized calls and response better.
 */
public abstract class AbstractCallback<T> implements Callback<T> {

    /**
     * Filters default responses according to http status.
     *
     * @param response
     */
    @Override
    public void onResponse(Response<T> response, Retrofit retrofit) {

        switch (response.code() / 100) {
            case 2:
            case 3:
                onSuccess(response);
                break;
            case 4:
            case 5:
                try {
                    onFailure(new Throwable(response.errorBody().source().readUtf8()));
                } catch (IOException e) {
                    onFailure(new Throwable(response.message()));
                }
        }


    }

    /**
     * an abstract method for http 2xx and 3xx responses.
     *
     * @param response
     */
    public abstract void onSuccess(Response<T> response);

    @Override
    public abstract void onFailure(Throwable t);

}
