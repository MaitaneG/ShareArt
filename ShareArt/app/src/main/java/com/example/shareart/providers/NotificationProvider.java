package com.example.shareart.providers;

import com.example.shareart.models.FCMBody;
import com.example.shareart.models.FCMResponse;
import com.example.shareart.retrofit.IFCMApi;
import com.example.shareart.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public NotificationProvider() {

    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClient(url).create(IFCMApi.class).send(body);
    }
}
