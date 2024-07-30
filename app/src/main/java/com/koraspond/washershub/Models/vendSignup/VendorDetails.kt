package com.koraspond.washershub.Models.vendSignup

data class VendorDetails(
    val address: String,
    val area: Int,
    val end_time: String,
    val lat: String,
    val long: String,
    val shop_name: String,
    val start_time: String,
    val timeslots_duration: Int,
    val user: Int,
    val vendor_capacity: Int,
    val vendor_category: Int
)