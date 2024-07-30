package com.koraspond.washershub.Models.getEarnings

data class list (
    val month: String,
    val order_ids: List<String>,
    val platform_fee: String,
    val total_commission: String,
    val total_customer_orders: Int,
    val total_customer_orders_earnings: String,
    val total_earnings: String,
    val total_remaining: String,
    val total_vendor_orders: Int,
    val total_vendor_orders_earnings: String,
    val year: Int
)