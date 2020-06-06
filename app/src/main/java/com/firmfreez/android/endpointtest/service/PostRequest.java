package com.firmfreez.android.endpointtest.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Интерфейс для POST запросов
 */
public interface PostRequest {
    @POST("{endpoint}")
    Call<ResponseBody> makeRequest(@Path("endpoint") String endpoint);
}
