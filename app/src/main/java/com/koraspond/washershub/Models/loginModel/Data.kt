package com.koraspond.washershub.Models.loginModel

data class Data(
    val email: String,
    val id: Int,
    val profile_completed: Boolean,
    val token: String,
    val user_info: UserInfo,
    val user_name: String,
    val user_role: UserRole,
    val vendor: Vendor
)