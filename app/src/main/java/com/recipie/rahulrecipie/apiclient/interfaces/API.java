package com.recipie.rahulrecipie.apiclient.interfaces;

import android.os.Bundle;


import retrofit.Callback;

/**
 * Created by developers on 24/10/16.
 */

public interface API<Request, Response, ServiceInterface> {
    /**
     * Builds Retrofit Object
     */
    public void buildRetrofit();

    /**
     * Builds Params
     */
    public void buildParams(Bundle params);

    /**
     * Execution of the API
     */
    public void execute(Callback<Response> callback);

    /**
     * Creates a request object of type <Request>
     */
    public Request createRequest();

    /**
     * Creates a service object of type <ServiceInterface>
     */
    public ServiceInterface createService();
}
