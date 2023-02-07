package com.allan.camvor.retrofit;

import com.allan.camvor.models.FCMBody;
import com.allan.camvor.models.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAQ3RDlLQ:APA91bEFiv8D9VEQT_ImYYL2VSs3TDoqj9-bMg60mYXVbphx87xP0G4UTkZN2ocAtDwGV3RuE9yrXExfGwn0OdIBAJEh3A69vy_c-9d5wtOFcExR5FNQfdfip9BH1yZs1bkCfvxb-rl2"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);
}
