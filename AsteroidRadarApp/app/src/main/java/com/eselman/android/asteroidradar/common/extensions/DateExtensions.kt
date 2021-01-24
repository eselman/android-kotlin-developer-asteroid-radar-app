package com.eselman.android.asteroidradar.common.extensions

import com.eselman.android.asteroidradar.common.Constants
import java.text.SimpleDateFormat
import java.util.*

fun Date.toStringFormat(): String {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(this)
}

fun String.toDateFormat(): Date {
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.parse(this)
}