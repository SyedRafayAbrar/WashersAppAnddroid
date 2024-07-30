package com.koraspond.washershub.Models.orderDetailModel

data class Vendor(
    val address: String,
    val area: Int,
    val avatar: Any,
    val end_time: String,
    val id: Int,
    val is_free_mode: Boolean,
    val is_premium: Boolean,
    val lat: Double,
    val long: Double,
    val shop_name: String,
    val start_time: String,
    val timeslots_duration: Int,
    val user: Int,
    val vendor_capacity: Int,
    val vendor_category: Int
)