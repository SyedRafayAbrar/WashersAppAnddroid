package com.koraspond.washershub.Models

data class CreqteORderRequest (var serviceid:Int,var orderDate:String,var orderTime:String,var orderEndTime:String,
    var paymentMethod:Int,var vendorID:Int,var total:Double,var isTime:Boolean,var variationid:Int)