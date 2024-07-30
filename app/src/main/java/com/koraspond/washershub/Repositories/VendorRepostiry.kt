package com.koraspond.washershub.Repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koraspond.washershub.Models.CreateORder
import com.koraspond.washershub.Models.getCategory.GetCategories
import com.koraspond.washershub.Models.getCitieis.GetCities
import com.koraspond.washershub.Models.getEarnings.GetAllEarnings
import com.koraspond.washershub.Models.getVariation.GetAllVariations
import com.koraspond.washershub.Models.getVendServices.GetServices
import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors
import com.koraspond.washershub.Models.orderDetailModel.OrderDetailModel
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel
import com.koraspond.washershub.Models.vendorDetails.VendorDetails
import com.koraspond.washershub.Models.vendorModels.getVendorOrder.GetVendorOrders
import com.koraspond.washershub.Resource
import com.pegasus.pakbiz.network.Api
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorRepostiry private constructor() {

    var isLoading: MutableLiveData<String> = MutableLiveData()
    lateinit var  _userData: MutableLiveData<Resource<GetAllVendors>?>
    lateinit var vendDetails: MutableLiveData<Resource<VendorDetails>?>
    lateinit var  timeSlots: MutableLiveData<Resource<GetTimeSlots>?>
    lateinit var createORder:MutableLiveData<Resource<CreateORder>?>
    lateinit var orderDetails:MutableLiveData<Resource<OrderDetailModel>?>
    lateinit var  orderList : MutableLiveData<Resource<GetVendorOrders>?>
    lateinit var  vendorServices :MutableLiveData<Resource<GetServices>?>
    lateinit var  getCategory :MutableLiveData<Resource<GetCategories>?>
     var  variations =MutableLiveData<Resource<GetAllVariations>?>()
     var  addVariation = MutableLiveData<Resource<GetAllVariations>?>()
    var  getAllEarnings = MutableLiveData<Resource<GetAllEarnings>?>()
    var  updateOrderList = MutableLiveData<Resource<UpdateStatusModel>?>()
    lateinit var cities:MutableLiveData<Resource<GetCities>>
    val userData: LiveData<Resource<GetAllVendors>?> get() = _userData

    companion object {
        @Volatile
        private var instance: VendorRepostiry? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: VendorRepostiry().also { instance = it }
        }
    }
    fun getOrderDetails(token: String,id:String): LiveData<Resource<OrderDetailModel>?>
    {
        orderDetails =  MutableLiveData<Resource<OrderDetailModel>?>()
        if (id.isEmpty()) {
            return orderDetails
        }

        isLoading.postValue("true")

        Api.client.getOrderDetail("token "+token, id).enqueue(object : Callback<OrderDetailModel> {
            override fun onResponse(call: Call<OrderDetailModel>, response: Response<OrderDetailModel>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    orderDetails.postValue(Resource.success(allVendors))
                } else {
                    orderDetails.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<OrderDetailModel>, t: Throwable) {
                isLoading.postValue("false")
                orderDetails.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return orderDetails
    }
    fun getRecentORders(token: String,vendorId:String): LiveData<Resource<GetVendorOrders>?> {

        orderList = MutableLiveData<Resource<GetVendorOrders>?>()
        if (token == null) {
            return orderList
        }

        isLoading.postValue("true")

        Api.client.getRecentOrders("token "+token,vendorId).enqueue(object : Callback<GetVendorOrders> {
            override fun onResponse(call: Call<GetVendorOrders>, response: Response<GetVendorOrders>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    orderList.postValue(Resource.success(allVendors))
                } else {
                    orderList.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetVendorOrders>, t: Throwable) {
                isLoading.postValue("false")
                orderList.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return orderList
    }

    fun getVendorServices(token: String,vendorId:String): LiveData<Resource<GetServices>?> {
        vendorServices = MutableLiveData<Resource<GetServices>?>()
        if (token == null) {
            return vendorServices
        }

        isLoading.postValue("true")

        Api.client.getVendServices("token "+token,vendorId).enqueue(object : Callback<GetServices> {
            override fun onResponse(call: Call<GetServices>, response: Response<GetServices>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    vendorServices.postValue(Resource.success(allVendors))
                } else {
                    orderList.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }



            override fun onFailure(call: Call<GetServices>, t: Throwable) {
                isLoading.postValue("false")
                vendorServices.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return vendorServices
    }
    fun getVariations(token: String): LiveData<Resource<GetAllVariations>?> {
        variations.postValue(null)
        if (token == null) {
            return variations
        }

        isLoading.postValue("true")

        Api.client.getAllVariations("token "+token).enqueue(object : Callback<GetAllVariations> {
            override fun onResponse(call: Call<GetAllVariations>, response: Response<GetAllVariations>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVendors = response.body()!!
                    variations.postValue(Resource.success(allVendors))
                } else {
                    variations.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }



            override fun onFailure(call: Call<GetAllVariations>, t: Throwable) {
                isLoading.postValue("false")
                variations.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return variations
    }

    fun addVariation(token: String, variation: String, id: String): LiveData<Resource<GetAllVariations>?> {
        addVariation.value = null

        if (token.isEmpty()) {
            return addVariation
        }

        isLoading.postValue("true")

        // Create the JSON structure
        val jsonArray = JsonArray()
        val arrayObj = JsonObject().apply {
            addProperty("name", variation)
            addProperty("vendor", id)
        }
        jsonArray.add(arrayObj)

        val jsonObject = JsonObject().apply {
            add("variations", jsonArray)
        }

        val body: RequestBody = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        Api.client.addVariation("token "+ token, body).enqueue(object : Callback<GetAllVariations> {
            override fun onResponse(call: Call<GetAllVariations>, response: Response<GetAllVariations>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVariations = response.body()!!
                    addVariation.postValue(Resource.success(allVariations))
                } else {
                    addVariation.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetAllVariations>, t: Throwable) {
                isLoading.postValue("false")
                addVariation.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return addVariation
    }

    fun getCategoryies(token: String): LiveData<Resource<GetCategories>?> {
        getCategory = MutableLiveData<Resource<GetCategories>?>()



        isLoading.postValue("true")






        Api.client.GetCategory("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe", 1).enqueue(object : Callback<GetCategories> {
            override fun onResponse(call: Call<GetCategories>, response: Response<GetCategories>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVariations = response.body()!!
                    getCategory.postValue(Resource.success(allVariations))
                } else {
                    getCategory.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetCategories>, t: Throwable) {
                isLoading.postValue("false")
                getCategory.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return getCategory
    }


    fun getCities(token: String): LiveData<Resource<GetCities>?> {
        cities = MutableLiveData<Resource<GetCities>>()



        isLoading.postValue("true")






        Api.client.getCities("Basic 8db88ff86fb9b0a4f4ff1e204b6ace5c04ad6fbad96617fc558819a2dd5c23fe",1).enqueue(object : Callback<GetCities> {
            override fun onResponse(call: Call<GetCities>, response: Response<GetCities>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVariations = response.body()!!
                    cities.postValue(Resource.success(allVariations))
                } else {
                    cities.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetCities>, t: Throwable) {
                isLoading.postValue("false")
                 cities.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return cities
    }

    fun getEarnings(token: String,duration:String,page:Int): LiveData<Resource<GetAllEarnings>?> {



        isLoading.postValue("true")






        Api.client.GetEarnings(duration,page,"token "+ token).enqueue(object : Callback<GetAllEarnings> {
            override fun onResponse(call: Call<GetAllEarnings>, response: Response<GetAllEarnings>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVariations = response.body()!!
                    getAllEarnings.postValue(Resource.success(allVariations))
                } else {
                    getAllEarnings.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<GetAllEarnings>, t: Throwable) {
                isLoading.postValue("false")
                getAllEarnings.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return getAllEarnings
    }

    fun updateOrderStatus(token: String,status:String,orderId:String,reason:String): LiveData<Resource<UpdateStatusModel>?> {



        isLoading.postValue("true")

        var jsonObject = JsonObject().apply {
            addProperty("status",status)
            addProperty("order_id",orderId)
            addProperty("reason",reason)



        }
        val body: RequestBody = jsonObject.toString()
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())





        Api.client.updateStatus("token "+ token,body).enqueue(object : Callback<UpdateStatusModel> {
            override fun onResponse(call: Call<UpdateStatusModel>, response: Response<UpdateStatusModel>) {
                isLoading.postValue("false")
                if (response.isSuccessful) {
                    val allVariations = response.body()!!
                    updateOrderList.postValue(Resource.success(allVariations))
                } else {
                    updateOrderList.postValue(Resource.error(null, "Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<UpdateStatusModel>, t: Throwable) {
                isLoading.postValue("false")
                updateOrderList.postValue(Resource.error(null, t.message ?: "Unknown error"))
            }
        })

        return updateOrderList
    }



}
