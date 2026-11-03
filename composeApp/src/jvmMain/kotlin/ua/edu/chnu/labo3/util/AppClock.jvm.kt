package ua.edu.chnu.labo3.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun nowEpochMillis(): Long = System.currentTimeMillis()

actual fun formatEpochMillis(epochMillis: Long): String =
    SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault()).format(Date(epochMillis))
