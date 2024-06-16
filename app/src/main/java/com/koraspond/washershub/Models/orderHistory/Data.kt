package com.koraspond.washershub.Models.orderHistory

data class Data(
    val assignee: Any,
    val commission_amount: Double,
    val commission_percentage: Double,
    val id: Int,
    val latest_status: Int,
    val order_date: String,
    val order_endtime_value: String,
    val order_time_value: String,
    val payment_method: Int,
    val service: Int,
    val service_variation: Int,
    val total: Double,
    val unique_identifier: String,
    val user: Int,
    val vendor: Int
)