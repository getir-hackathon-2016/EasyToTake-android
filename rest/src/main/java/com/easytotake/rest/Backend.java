package com.easytotake.rest;

import com.easytotake.rest.model.BasketModel;
import com.easytotake.rest.model.Category;
import com.easytotake.rest.model.Messenger;
import com.easytotake.rest.model.Product;
import com.easytotake.rest.util.Constants;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface Backend {

    @GET(Constants.Rest.MESSENGERS)
    Call<List<Messenger>> messengers();

    // TODO get by Messenger
    @GET(Constants.Rest.CATEGORIES)
    Call<List<Category>> categories(@Query("page") int page, @Query("take") int take);

    // TODO get by Categories
    @GET(Constants.Rest.PRODUCTS)
    Call<List<Product>> products(@Query("page") int page, @Query("take") int take, @Query("parentOid") String parentOid);

    @GET(Constants.Rest.PRODUCTS_TOP)
    Call<List<Product>> topProducts(@Query("take") int take);

    // TODO get by shopping card
    @GET(Constants.Rest.SHOPPING_CARD)
    Call<List<Product>> shoppingCard(@Query("userOid") String userOid);

    @POST(Constants.Rest.UPDATE_SHOPPING_CARD)
    Call<BasketModel> updateShoppingCard(@Body Product product);

    @GET(Constants.Rest.GET_SHOPPING_CARD_COUNT)
    Call<BasketModel> getShoppingCardCount(@Query("userOid") String userOid);
}
