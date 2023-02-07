package com.allan.camvor.providers;

import com.allan.camvor.models.FCMBody;
import com.allan.camvor.models.FCMResponse;
import com.allan.camvor.retrofit.IFCMApi;
import com.allan.camvor.retrofit.RetrofitClient;

import retrofit2.Call;

public class NotificationProvider {

    private String url = "https://fcm.googleapis.com";

    public  NotificationProvider() {
    }

    public Call<FCMResponse> sendNotification(FCMBody body) {
        return RetrofitClient.getClientObject(url).create(IFCMApi.class).send(body);
    }
}
