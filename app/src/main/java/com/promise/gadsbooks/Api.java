package com.promise.gadsbooks;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface Api {

    @POST("send")
    Call<ResponseBody> sendMessage(
            @HeaderMap Map<String, String> headers,
            @Body FCMessage FCMessage
            );
}
