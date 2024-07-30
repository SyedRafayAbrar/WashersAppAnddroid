package com.koraspond.washershub.Models.orderDetailModel

data class Data(
    val assignee: Any,
    val commission_amount: Double,
    val commission_percentage: Double,
    val contact_number: String,
    val id: Int,
    val latest_status: LatestStatus,
    val order_date: String,
    val order_endtime_value: String,
    val order_time_value: String,
    val payment_method: PaymentMethod,
    val service: Service,
    val service_variation: ServiceVariation,
    val total: Double,
    val unique_identifier: String,
    val user: User,
    val variation: Variation,
    val vendor: Vendor,
    var is_reviewed:Boolean = false
)