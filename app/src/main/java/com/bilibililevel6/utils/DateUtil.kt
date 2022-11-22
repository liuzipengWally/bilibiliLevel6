package com.bilibililevel6.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * author：liuzipeng
 * time: 2022/11/21 00:03
 */

enum class DateFormatMode(val mode: String) {
    YEAR_MONTH_DAY("yyyy-MM-dd"),
    DURATION("HH:mm:ss")
}

infix fun Long.dateFormatTo(mode: DateFormatMode): String {
    val sdf = SimpleDateFormat(mode.mode, Locale.PRC)
    return sdf.format(this)
}

fun Long.secondToDurationString(): String {
    val sec = this % 60
    val min = (this - sec) / 60
    val secStr = if (sec < 10) {
        "0$sec"
    } else {
        sec.toString()
    }
    val minStr = if (min < 10) {
        "0$min"
    } else {
        min.toString()
    }
    return "$secStr:$minStr"
}