package com.koraspond.washershub.Models.vendSignup

data class Data(
    val email: String,
    val id: Int,
    val token: String,
    val user_name: String,
    val vendor_details: VendorDetails
)