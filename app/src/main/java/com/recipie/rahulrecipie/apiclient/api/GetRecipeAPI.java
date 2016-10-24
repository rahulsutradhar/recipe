package com.recipie.rahulrecipie.apiclient.api;

import android.os.Bundle;

import com.google.gson.Gson;
import com.recipie.rahulrecipie.apiclient.builder.APIBuilder;
import com.recipie.rahulrecipie.apiclient.interfaces.API;
import com.recipie.rahulrecipie.apiclient.interfaces.RecipeService;
import com.recipie.rahulrecipie.apiclient.request.RecipeRequest;
import com.recipie.rahulrecipie.apiclient.response.RecipeResponse;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by developers on 24/10/16.
 */

public class GetRecipeAPI extends APIBuilder implements API<RecipeRequest, RecipeResponse, RecipeService> {
    /**
     * Retrofit builder used for networking
     */
    private Retrofit retrofit;

    /**
     * RecipeId
     */
    private String recipeId;

    /**
     * Constructor
     */
    public GetRecipeAPI() {
        super();
    }

    public static GetRecipeAPI build(Bundle param) {
        GetRecipeAPI getRecipeAPI = new GetRecipeAPI();
        getRecipeAPI.buildParams(param);
        getRecipeAPI.buildRetrofit();
        return getRecipeAPI;
    }

    /**
     * Builds and sets the retrofit object out of converter factory
     */
    @Override
    public void buildRetrofit() {
        super.buildRetrofit();
        this.retrofit = super.getRetrofit();
    }

    /**
     * Validates and creates the parameters requested for this request
     */
    @Override
    public void buildParams(Bundle params) {
        recipeId = params.getString("recipe_id");
    }

    /**
     * Creates the custom GsonConverterFactory
     *
     * @return GsonConverterFactory
     */
    private GsonConverterFactory createConverterFactory() {
        Gson gson = new Gson();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);

        return gsonConverterFactory;
    }

    @Override
    public void execute(Callback<RecipeResponse> callback) {
        RecipeRequest recipeRequest = createRequest();
        RecipeService recipeService = createService();

        Call<RecipeResponse> call = recipeService.getRecipeType(recipeRequest);
        call.enqueue(callback);
    }

    @Override
    public RecipeRequest createRequest() {
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setRecipeId(recipeId);
        return recipeRequest;
    }

    @Override
    public RecipeService createService() {
        RecipeService recipeService = retrofit.create(RecipeService.class);
        return recipeService;
    }
}
