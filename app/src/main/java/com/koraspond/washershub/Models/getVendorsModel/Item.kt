package com.koraspond.washershub.Models.getVendorsModel

data class Item(
    val address: String,
    val avatar: Any,
    val end_time: String,
    val id: Int,
    val is_closed: Boolean,
    val lat: Double,
    val long: Double,
    val shop_name: String,
    val start_time: String,
    val vendor_category: VendorCategory
)