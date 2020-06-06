package com.firmfreez.android.endpointtest.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Интерфейс для PUT запросов
 */
public interface PutRequest {
    @PUT("{endpoint}")
    Call<ResponseBody> makeRequest(@Path("endpoint") String endpoint);
}
