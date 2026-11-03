package ua.edu.chnu.labo3.data

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ua.edu.chnu.labo3.db.AppDatabase

/**
 * Android driver: backed by [AndroidSqliteDriver], which stores the database in
 * the app's private files as `reminders.db`. The [Context] is supplied by Koin.
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = context,
            name = "reminders.db",
        )
}
