package com.koraspond.washershub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.koraspond.washershub.Models.CreateORder
import com.koraspond.washershub.Models.CreqteORderRequest
import com.koraspond.washershub.Models.getVendorsModel.GetAllVendors
import com.koraspond.washershub.Models.orderHistory.OrderHistory
import com.koraspond.washershub.Models.timeSlotModels.GetTimeSlots
import com.koraspond.washershub.Models.vendorDetails.VendorDetails
import com.koraspond.washershub.Repositories.VendorRepo
import com.koraspond.washershub.Resource

class VendorViewModel(private val vendorRepo: VendorRepo) : ViewModel() {
    val isLoading: LiveData<String> = vendorRepo.isLoading

    fun getVendors(token: String, area: Int, cat: Int): LiveData<Resource<GetAllVendors>?> {
        return vendorRepo.getVendors(area, cat, token)
    }

    fun getVendorDetail(token: String, id:Int): LiveData<Resource<VendorDetails>?> {
        return vendorRepo.getVendorsDetails(token,id)
    }

    fun getTimeSlots(token: String, vid:Int,sid:Int): LiveData<Resource<GetTimeSlots>?> {
        return vendorRepo.getTimeSlots(token,vid, sid)
    }

    fun createORder(token: String, req:CreqteORderRequest): LiveData<Resource<CreateORder>?> {
        return vendorRepo.createORder(token,req)
    }
    fun getHistory(token: String): LiveData<Resource<OrderHistory>?> {
        return vendorRepo.getHistory(token)
    }
}
