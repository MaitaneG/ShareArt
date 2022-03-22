package com.example.shareart.retrofit;

import com.example.shareart.models.FCMBody;
import com.example.shareart.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAo0xA6ZI:APA91bHN8IIhLw-m9NAv-yjEtyAvxuHe75fpezFj7aK-3tlgLRrNrx6GlAvesCn7xbBCf7kcGgKg3EAZ-ESaZC_dLgorykoZ1XY4mq32rajcVt43O5xsWE2PYG4fCSc_1alSBqEleduN"
    })
    @POST("fcm/send")
    Call<FCMResponse>send(@Body FCMBody body);
}
