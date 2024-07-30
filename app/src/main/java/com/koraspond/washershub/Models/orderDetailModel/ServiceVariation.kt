package com.koraspond.washershub.Models.orderDetailModel

data class ServiceVariation(
    val amount: Double,
    val id: Int,
    val is_time_constraint: Boolean,
    val service: Int,
    val variation: Int
)