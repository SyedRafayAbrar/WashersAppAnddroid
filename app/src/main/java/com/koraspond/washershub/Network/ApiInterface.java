package com.koraspond.washershub.Network;

import com.koraspond.washershub.Models.AddReview.AddReview;
import com.koraspond.washershub.Models.CreateORder;
import com.koraspond.washershub.Models.GetVendorService;
import com.koraspond.washershub.Models.SignupModel.SignupModel;
import com.koraspond.washershub.Models.addService.AddService;
import com.koraspond.washershub.Models.getCategory.GetCategories;
import com.koraspond.washershub.Models.getCitieis.GetCities;
import com.koraspond.washershub.Models.getEarnings.GetAllEarnings;
import com.koraspond.washershub.Models.getReview.GetReviews;
import com.koraspond.washershub.Models.getVariation.GetAllVariations;
import com.koraspond.washershub.Models.getVendServices.GetServices;

import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors;
import com.koraspond.washershub.Models.loginModel.LoginModel;
import com.koraspond.washershub.Models.orderDetailModel.OrderDetailModel;
import com.koraspond.washershub.Models.orderHistory.OrderHistory;
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots;
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel;
import com.koraspond.washershub.Models.vendSignup.VendorSignup;
import com.koraspond.washershub.Models.vendorDetails.VendorDetails;
import com.koraspond.washershub.Models.vendorModels.getVendorOrder.GetVendorOrders;

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
    Call<GetAllVendors> getVendors(@Header("Authorization") String token, @Query("area")  int area,@Query("category")  int category,@Query("page") int page);

    @GET("vendors")
    Call<GetAllVendors> getVendors(@Header("Authorization") String token, @Query("area")  int area,@Query("category")  int category,@Query("page") int page,@Query("lat") Double lat,@Query("long") Double lng,@Query("is_nearby") int isnearby);

    @GET("get_vendor")
    Call<VendorDetails> getVendorDetail(@Header("Authorization") String token, @Query("vendor_id")  int vendor_id);
    @GET("get_timeslots")

    Call<GetTimeSlots> getTimeSlots(@Header("Authorization") String token, @Query("vendor_id")  int vendor_id, @Query("service_id") int serviceID);
    @POST("create_order")
    Call<CreateORder> createORder(@Header("Authorization") String token, @Body  RequestBody requestBody);

    @GET("get_orders")
    Call<OrderHistory> getHistory(@Header("Authorization") String token,@Query("page") int page);

    @GET("get_order")
    Call<OrderDetailModel> getOrderDetail(@Header("Authorization") String token, @Query("order_id") String order_id);

    @POST("add_rating")
    Call<AddReview> addRating(@Header("Authorization") String token, @Body  RequestBody requestBody);


    //vendor apis
    @GET("get_recent_order")
    Call<GetVendorOrders> getRecentOrders(@Header("Authorization") String token, @Query("vendor") String vendor);

    @GET("get_services")
    Call<GetServices> getVendServices(@Header("Authorization") String token, @Query("vendor_id") String vendor);

    @GET("vendor/get_variation")
    Call<GetAllVariations> getAllVariations(@Header("Authorization") String token);

    @POST("vendor/add_variation")
    Call<GetAllVariations> addVariation(@Header("Authorization") String token, @Body  RequestBody requestBody);

    @GET("get_categories")
    Call<GetCategories> GetCategory(@Header("Authorization") String token, @Query("category_id")  int category_id);

    @POST("vendor/add_service")
    Call<AddService> addService(@Header("Authorization") String token,  @Body  RequestBody requestBody);



    @GET("admin/get_area")
    Call<GetCities> getCities(@Header("Authorization") String token,@Query("city_id") int city);


    @POST("signup")
    Call<VendorSignup> vendorSignup(@Body  RequestBody requestBody , @Header("Authorization") String token);

    @GET("vendor/get_account")
    Call<GetAllEarnings> GetEarnings(@Query("duration") String duration,@Query("page") int page, @Header("Authorization") String token);


    @PUT("update_status")
    Call<UpdateStatusModel> updateStatus( @Header("Authorization") String token, @Body  RequestBody requestBody);

    @GET("get_ratings")
    Call<GetReviews> getReviews(@Header("Authorization") String token, @Query("vendor")  String vendor_id);

    @GET("admin/get_cities")
    Call<GetCities> getAllCities(@Header("Authorization") String token, @Query("country_id")  int country_id);





}
