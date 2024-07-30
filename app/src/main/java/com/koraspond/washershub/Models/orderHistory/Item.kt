package com.koraspond.washershub.Models.orderHistory

data class Item(
    val id: Int,
    val latest_status: LatestStatus,
    val order_date: String,
    val order_time_value: String,
    val payment_method: PaymentMethod,
    val total: Double,
    val unique_identifier: String,
    val vendor: Vendor
)