package com.bilibililevel6.login.entity

/**
 * author：liuzipeng
 * time: 2022/12/21 20:14
 */
data class LoginResultResponse(
    val url: String,
    val refresh_token: String,
    val timestamp: Long,
    val code: Int,
    val message: String
)
