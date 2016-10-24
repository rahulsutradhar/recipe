package com.recipie.rahulrecipie.apiclient.interfaces;

import com.recipie.rahulrecipie.apiclient.request.RecipeRequest;
import com.recipie.rahulrecipie.apiclient.response.RecipeResponse;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by developers on 24/10/16.
 */

public interface RecipeService {

    @POST("/testapi/getRecipeType.php")
    Call<RecipeResponse> getRecipeType(@Body RecipeRequest recipeRequest);
}
