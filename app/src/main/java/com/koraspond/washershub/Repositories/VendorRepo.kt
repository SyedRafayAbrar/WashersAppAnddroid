package com.koraspond.washershub.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koraspond.washershub.Models.CreateORder
import com.koraspond.washershub.Models.CreqteORderRequest
import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors
import com.koraspond.washershub.Models.orderHistory.OrderHistory
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots
import com.koraspond.washershub.Models.vendorDetails.Vendor
import com.koraspond.washershub.Models.vendorDetails.VendorDetails
import com.koraspond.washershub.Resource
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorRepo private constructor() {

    var isLoading: MutableLiveData<String> = MutableLiveData()
    private val _userData = MutableLiveData<Resource<GetAllVendors>?>()
    private val vendDetails = MutableLiveData<Resource<VendorDetails>?>()
    private val timeSlots = MutableLiveData<Resource<GetTimeSlots>?>()
    private val createORder = MutableLiveData<Resource<CreateORder>?>()
    private val orderHistory = MutableLiveData<Resource<OrderHistory>?>()
    val userData: LiveData<Resource<GetAllVendors>?> get() = _userData

    companion object {
        @Volatile
        private var instance: VendorRepo? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: VendorRepo().also { instance = it }
        }
    }

    fun getVendors(area: Int, cat: Int, token: String): LiveData<Resource<GetAllVendors>?> {
        if (area == -1) {
            return _userData
        }

        isLoading.postValue("true")

        Api.client.getVendors("token "+token, area, cat).enqueue(object : Callback<GetAllVendors> {
            override fun onResponse(call: Call<GetAllVendors>, response: Response<GetAllVendors>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    _userData.postValue(Resource.success(allVendors))
                } else {
                    _userData.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetAllVendors>, t: Throwable) {
                isLoading.postValue("false")
                _userData.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return _userData
    }
    fun getVendorsDetails(token: String,id:Int): LiveData<Resource<VendorDetails>?> {
        if (id == -1) {
            return vendDetails
        }

        isLoading.postValue("true")

        Api.client.getVendorDetail("token "+token, id).enqueue(object : Callback<VendorDetails> {
            override fun onResponse(call: Call<VendorDetails>, response: Response<VendorDetails>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    vendDetails.postValue(Resource.success(allVendors))
                } else {
                    vendDetails.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<VendorDetails>, t: Throwable) {
                isLoading.postValue("false")
                vendDetails.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return vendDetails
    }

    fun getTimeSlots(token: String,vid:Int,sid:Int): LiveData<Resource<GetTimeSlots>?> {
        if (vid == -1) {
            return timeSlots
        }

        isLoading.postValue("true")

        Api.client.getTimeSlots("token "+token,vid,sid).enqueue(object : Callback<GetTimeSlots> {
            override fun onResponse(call: Call<GetTimeSlots>, response: Response<GetTimeSlots>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    timeSlots.postValue(Resource.success(allVendors))
                } else {
                    timeSlots.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetTimeSlots>, t: Throwable) {
                isLoading.postValue("false")
                timeSlots.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return timeSlots
    }

    fun getHistory(token: String): LiveData<Resource<OrderHistory>?> {
        if (token == null) {
            return orderHistory
        }

        isLoading.postValue("true")

        Api.client.getHistory("token "+token).enqueue(object : Callback<OrderHistory> {
            override fun onResponse(call: Call<OrderHistory>, response: Response<OrderHistory>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    orderHistory.postValue(Resource.success(allVendors))
                } else {
                    orderHistory.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<OrderHistory>, t: Throwable) {
                isLoading.postValue("false")
                orderHistory.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return orderHistory
    }
    fun createORder(token: String,request:CreqteORderRequest): LiveData<Resource<CreateORder>?> {
        if (token == null) {
            return createORder
        }

        isLoading.postValue("true")

        val jsonObject = JSONObject().apply {
            put("service_id", request.serviceid)
            put("order_date", request.orderDate)

            put("order_time", request.orderTime)
            put("order_endtime_value", request.orderTime)
            put("payment_method", request.paymentMethod)

            put("vendor_id", request.vendorID)
            put("total", request.total)

            put("is_time_constraint", request.isTime)
            put("variation_id", request.variationid)

        }
        val body: RequestBody
        body = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            jsonObject.toString()
        )

        Api.client.createORder("token "+token,body).enqueue(object : Callback<CreateORder> {
            override fun onResponse(call: Call<CreateORder>, response: Response<CreateORder>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    createORder.postValue(Resource.success(allVendors))
                } else {
                    createORder.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<CreateORder>, t: Throwable) {
                isLoading.postValue("false")
                createORder.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return createORder
    }
}
