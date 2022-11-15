package com.bilibililevel6.net

data class BaseDataOfWeb<T>(
    val code: Int,
    val data: T,
    val message: String,
    val ttl: Int
)