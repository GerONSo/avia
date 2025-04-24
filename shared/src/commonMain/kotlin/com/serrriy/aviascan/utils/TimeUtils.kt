package com.serrriy.aviascan.utils

object TimeUtils {
    private val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    fun getMonth(num: Int): String {
        return months[num - 1]
    }
}