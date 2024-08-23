package com.koraspond.washershub.Models.vendorDetails

data class Data(
    val address: String,
    val area: String,
    val avatar: String,
    val average_rating: Double,
    val end_time: String,
    val fields: List<Field>,
    val id: Int,
    val is_closed: Boolean,
    val is_free_mode: Boolean,
    val is_premium: Boolean,
    val lat: Double,
    val long: Double,
    val services: List<Service>,
    val shop_name: String,
    val start_time: String,
    val timeslots_duration: Int,
    val user: Int,
    val vendor_capacity: Int,
    val vendor_category: Int
)