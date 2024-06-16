package com.koraspond.washershub.Models.getVendorsModel

data class Data(
    val address: String,
    val avatar: Any,
    val end_time: String,
    val id: Int,
    val lat: Double,
    val long: Double,
    val shop_name: String,
    val start_time: String,
    val vendor_category: VendorCategory
)