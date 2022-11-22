package com.bilibililevel6.utils

/**
 * author：liuzipeng
 * time: 2022/11/22 23:34
 */
fun Int.toThousandString(): String {
    return if (this > 10000) "${this / 10000}万" else this.toString()
}