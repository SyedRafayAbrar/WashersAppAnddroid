package com.koraspond.washershub.Network;

import com.koraspond.washershub.Models.CreateORder;
import com.koraspond.washershub.Models.SignupModel.SignupModel;
import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors;
import com.koraspond.washershub.Models.loginModel.LoginModel;
import com.koraspond.washershub.Models.orderHistory.OrderHistory;
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots;
import com.koraspond.washershub.Models.vendorDetails.VendorDetails;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    //signup
    @POST("signup")
    Call<SignupModel> userSignup(@Header("Authorization") String token ,@Body  RequestBody requestBody);


    //signup
    @POST("login")
    Call<LoginModel> login(@Header("Authorization") String token,@Body  RequestBody requestBody);

    @GET("vendors")
    Call<GetAllVendors> getVendors(@Header("Authorization") String token, @Query("area")  int area,@Query("category")  int category);
    @GET("get_vendor")
    Call<VendorDetails> getVendorDetail(@Header("Authorization") String token, @Query("vendor_id")  int vendor_id);
    @GET("get_timeslots")
    Call<GetTimeSlots> getTimeSlots(@Header("Authorization") String token, @Query("vendor_id")  int vendor_id, @Query("service_id") int serviceID);
    @POST("create_order")
    Call<CreateORder> createORder(@Header("Authorization") String token, @Body  RequestBody requestBody);

    @GET("get_orders")
    Call<OrderHistory> getHistory(@Header("Authorization") String token);



}
