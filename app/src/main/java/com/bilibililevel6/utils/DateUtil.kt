package com.bilibililevel6.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * author：liuzipeng
 * time: 2022/11/21 00:03
 */

enum class DateFormatMode(val mode: String) {
    YEAR_MONTH_DAY("yyyy-MM-dd"),
    HOUR_MIN_SEC("HH:mm:ss"),
    MIN_SEC("mm:ss")
}

infix fun Long.dateFormatTo(mode: DateFormatMode): String {
    val sdf = SimpleDateFormat(mode.mode, Locale.PRC)
    return sdf.format(this)
}

fun Long.secondToDurationString(): String {
    val ms = this * 1000
    return ms dateFormatTo DateFormatMode.MIN_SEC
}