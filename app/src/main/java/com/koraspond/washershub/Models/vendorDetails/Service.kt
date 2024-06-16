package com.koraspond.washershub.Models.vendorDetails

data class Service(
    val id: Int,
    val image: String,
    val is_time_constraint: Boolean,
    val service_approx_time: String,
    val service_capacity: Int,
    val service_charges: String,
    val service_description: String,
    val service_name: String,
    val variations: List<Variation>?,
    val vendor: Vendor
)