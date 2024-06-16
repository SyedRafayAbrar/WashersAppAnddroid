package com.koraspond.washershub.Models.vendorDetails

data class Variation(
    val amount: Double,
    val id: Int,
    val is_time_constraint: Boolean,
    val variation: VariationX
)