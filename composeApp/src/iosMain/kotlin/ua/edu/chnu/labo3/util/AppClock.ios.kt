package ua.edu.chnu.labo3.util

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

actual fun nowEpochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000.0).toLong()

actual fun formatEpochMillis(epochMillis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis / 1000.0)
    val formatter = NSDateFormatter().apply { dateFormat = "dd.MM.yyyy HH:mm:ss" }
    return formatter.stringFromDate(date)
}
