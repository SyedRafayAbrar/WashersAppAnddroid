package com.koraspond.washershub.Models.getVendorsModel

data class Data(
    val current_page: String,
    val items: List<Item>?,
    val total_items: Int,
    val total_pages: Int
)