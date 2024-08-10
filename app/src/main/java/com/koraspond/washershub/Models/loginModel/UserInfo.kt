package com.koraspond.washershub.Models.loginModel

data class UserInfo(
    val contact_number: String,
    val id: Int,
    val image: String?,
    val is_active: Boolean,
    val role: Int,
    val user: Int
)