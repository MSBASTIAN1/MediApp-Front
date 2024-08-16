package com.puce.androidmed.network;

import com.puce.androidmed.models.AuthenticationRequest;
import com.puce.androidmed.models.AuthenticationResponse;
import com.puce.androidmed.models.MedicineResponse;
import com.puce.androidmed.models.Recommendation;
import com.puce.androidmed.models.Reminder;
import com.puce.androidmed.models.ReminderResponse;
import com.puce.androidmed.models.User;
import com.puce.androidmed.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("users/authenticate")
    Call<AuthenticationResponse> authenticateUser(@Body AuthenticationRequest request);

    @POST("admins/authenticate")
    Call<AuthenticationResponse> authenticateAdmin(@Body AuthenticationRequest request);

    @POST("users/insert")
    Call<UserResponse> registerUser(@Body User user);

    @POST("recommendations/insert")
    Call<Recommendation> sendRecommendation(@Body Recommendation recommendation);

    @GET("medicaments/select")
    Call<MedicineResponse> getMedicines();

    @POST("reminders/insert")
    Call<Reminder> insertReminder(@Body Reminder reminder);

    @GET("reminders/select")
    Call<ReminderResponse> getReminders();

    @PUT("reminders/update")
    Call<Reminder> updateReminder(@Body Reminder reminder);


    @DELETE("reminders/delete/{id}")
    Call<Void> deleteReminder(@Path("id") String reminderId);
}
