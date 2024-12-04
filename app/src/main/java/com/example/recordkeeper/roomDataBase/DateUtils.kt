package com.example.recordkeeper.roomDataBase

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentFormattedDate(): String {
    val formatter = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
    return formatter.format(Date())
}
