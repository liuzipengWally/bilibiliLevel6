package com.bilibililevel6.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * author：liuzipeng
 * time: 2022/11/21 00:03
 */

enum class DateFormatMode(mode: String) {
    YEAR_MONTH_DAY("yyyy-MM-dd"),
    DURATION("HH:mm:ss")
}

infix fun Long.dateFormatTo(mode: DateFormatMode): String {
    val sdf = SimpleDateFormat(mode.ordinal.toString(), Locale.PRC)
    return sdf.format(Date(this))
}