package com.firmfreez.android.endpointtest.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Интерфейс GET запроса
 */
public interface GetRequest {
    @GET("{endpoint}")
    Call<ResponseBody> makeRequest(@Path("endpoint") String endpoint);
}
