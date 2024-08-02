package com.koraspond.washershub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.koraspond.washershub.Models.CreateORder
import com.koraspond.washershub.Models.CreqteORderRequest
import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors
import com.koraspond.washershub.Models.orderDetailModel.OrderDetailModel
import com.koraspond.washershub.Models.orderHistory.OrderHistory
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots
import com.koraspond.washershub.Models.vendorDetails.VendorDetails
import com.koraspond.washershub.Repositories.CustomerRepos
import com.koraspond.washershub.Resource

class VendorViewModel(private val customerRepos: CustomerRepos) : ViewModel() {
    val isLoading: LiveData<String> = customerRepos.isLoading

//    fun getVendors(token: String, area: Int, cat: Int,page:Int): LiveData<Resource<GetAllVendors>?> {
//        return customerRepos.getVendors(area, cat, token,page)
//    }
fun getVendors(token: String, area: Int, cat: Int, page: Int): LiveData<Resource<GetAllVendors>?> {
    return customerRepos.getVendors(area, cat, token, page)
}

    fun getNearbyVendors(token: String, area: Int, cat: Int, page: Int, lat: Double, lng: Double): LiveData<Resource<GetAllVendors>?> {
        return customerRepos.getVendors(area, cat, token, page, 1, lat, lng)
    }
    fun getVendorDetail(token: String, id:Int): LiveData<Resource<VendorDetails>?> {
        return customerRepos.getVendorsDetails(token,id)
    }

    fun getTimeSlots(token: String, vid:Int,sid:Int): LiveData<Resource<GetTimeSlots>?> {
        return customerRepos.getTimeSlots(token,vid, sid)
    }

    fun createORder(token: String, req:CreqteORderRequest): LiveData<Resource<CreateORder>?> {
        return customerRepos.createORder(token,req)
    }
    fun getHistory(token: String,page:Int): LiveData<Resource<OrderHistory>?> {
        return customerRepos.getHistory(token,page)
    }
    fun obserHist(): LiveData<Resource<OrderHistory>?> {
        return customerRepos.orderHistory
    }


    fun getOrderDetail(token: String,id:String): LiveData<Resource<OrderDetailModel>?> {
        return customerRepos.getOrderDetails(token,id)
    }
}
