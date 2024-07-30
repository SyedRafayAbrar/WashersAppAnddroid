package com.koraspond.washershub.Models.loginModel

data class Vendor(
    val address: String,
    val avatar: String,
    val end_time: String,
    val id: Int,
    val is_closed: Boolean,
    val lat: Double,
    val long: Double,
    val shop_name: String,
    val start_time: String,
    val vendor_category: VendorCategory
)