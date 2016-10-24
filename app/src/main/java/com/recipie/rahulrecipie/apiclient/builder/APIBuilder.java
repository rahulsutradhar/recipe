package com.recipie.rahulrecipie.apiclient.builder;

import com.recipie.rahulrecipie.global.Constant;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by developers on 24/10/16.
 */

public class APIBuilder {
    /**
     * Default Constructor
     */
    public APIBuilder(){
    }

    /**
     * Builds the retrofit object
     */
    public void buildRetrofit(){
        this.retrofit = create();
    }

    /**
     * Create default retrofit builder
     */
    public Retrofit create(){
        Retrofit retrofit = buildDefaultRetrofit();

        return retrofit;
    }

    /**
     * Retrofit for the API calls
     */
    private Retrofit retrofit;

    /**
     * Creates retrofit with custom GsonConverterFactory and GsonBuilder
     */
    public Retrofit create(GsonConverterFactory gsonConverterFactory){
        return buildRetrofit(gsonConverterFactory);
    }

    /**
     * Returns default retrofit builder with MoshiConverterFactory
     * @return Retrofit
     */
    private Retrofit buildDefaultRetrofit(){
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
        return buildRetrofit(gsonConverterFactory);
    }

    /**
     * Returns a retrofit builder
     * @param gsonConverterFactory
     * @return Retrofit
     */
    private Retrofit buildRetrofit(GsonConverterFactory gsonConverterFactory){
        // Define the interceptor, add authentication headers
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request newRequest;
                newRequest = chain.request().newBuilder().build();

                return chain.proceed(newRequest);
            }
        };

        // Add the interceptor to OkHttpClient
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.interceptors().add(interceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.HOST)
                .addConverterFactory(gsonConverterFactory)
                .client(client)
                .build();

        return retrofit;
    }

    /**
     * Getters and setters
     */
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}
