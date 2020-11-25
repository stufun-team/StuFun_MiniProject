package com.example.stufun.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAALbv1vig:APA91bEaTJsD6JLHaOt42Zv62Hw2jhf0QlDNXtc9eijLik2MTrgccdtiwGDySWLn9PyA6ksuLFpKM8HqA1CizTCD9qzTlbTdQgSkqQm-oMUlzmBqJ43d7uxNCeP8sg54BRbZGgyznPTg"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
