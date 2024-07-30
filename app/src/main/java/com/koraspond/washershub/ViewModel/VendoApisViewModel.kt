package com.koraspond.washershub.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.koraspond.washershub.Models.getCategory.GetCategories
import com.koraspond.washershub.Models.getEarnings.GetAllEarnings
import com.koraspond.washershub.Models.getVariation.GetAllVariations
import com.koraspond.washershub.Models.getVendServices.GetServices
import com.koraspond.washershub.Models.orderDetailModel.OrderDetailModel
import com.koraspond.washershub.Models.updateStatus.UpdateStatusModel
import com.koraspond.washershub.Models.vendorModels.getVendorOrder.GetVendorOrders
import com.koraspond.washershub.Repositories.VendorRepostiry
import com.koraspond.washershub.Resource

class VendoApisViewModel(private val vendorRepostiry: VendorRepostiry) : ViewModel() {
    val isLoading: LiveData<String> = vendorRepostiry.isLoading

    fun getVendorOrders(token: String, vendorID:String): LiveData<Resource<GetVendorOrders>?> {
        return vendorRepostiry.getRecentORders(token, vendorID)
    }

    fun getOrderDetail(token: String,id:String): LiveData<Resource<OrderDetailModel>?> {
        return vendorRepostiry.getOrderDetails(token, id)
    }

    fun getServices(token: String,id:String): LiveData<Resource<GetServices>?> {
        return vendorRepostiry.getVendorServices(token, id)
    }

    fun getVariations(token: String): LiveData<Resource<GetAllVariations>?> {
        return vendorRepostiry.getVariations(token)
    }

    fun addVariation(token: String,variation:String,id:String): LiveData<Resource<GetAllVariations>?> {
        return vendorRepostiry.addVariation(token,variation,id)
    }
    fun getAddVariation():LiveData<Resource<GetAllVariations>?>{
        return vendorRepostiry.addVariation
    }

    fun observeVariation():LiveData<Resource<GetAllVariations>?>{
        return vendorRepostiry.variations
    }

    fun getCat(token: String): LiveData<Resource<GetCategories>?> {
        return vendorRepostiry.getCategoryies(token)
    }

    fun getAllEarnings(token: String,duration:String,page:Int): LiveData<Resource<GetAllEarnings>?> {
        return vendorRepostiry.getEarnings(token,duration,page)
    }

    fun observeEarning(): LiveData<Resource<GetAllEarnings>?> {
        return vendorRepostiry.getAllEarnings
    }

    fun getUpdateStatus(token: String,status:String,orderId:String,reason:String):LiveData<Resource<UpdateStatusModel>?>{
        return vendorRepostiry.updateOrderStatus(token,status,orderId,reason)
    }
    fun observeupdateStatus(): LiveData<Resource<UpdateStatusModel>?> {
        return vendorRepostiry.updateOrderList
    }





}
