package com.recipie.rahulrecipie.apiclient.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by developers on 24/10/16.
 */

public class RecipeRequest {

    /**
     * Recipe id
     */
    @SerializedName("RTid")
    private String recipeId;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }
}
