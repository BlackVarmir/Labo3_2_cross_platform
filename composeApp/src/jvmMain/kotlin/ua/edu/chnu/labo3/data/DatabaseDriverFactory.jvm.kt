package ua.edu.chnu.labo3.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import ua.edu.chnu.labo3.db.AppDatabase
import java.io.File

/**
 * JVM/Desktop driver: a JDBC SQLite database stored under the user's home
 * directory (`~/.labo3/reminders.db`) so reminders survive app restarts. The
 * schema is created only the first time the file is generated.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbFile = File(System.getProperty("user.home"), ".labo3/reminders.db")
        dbFile.parentFile?.mkdirs()
        val isNew = !dbFile.exists()

        val driver = JdbcSqliteDriver("jdbc:sqlite:${dbFile.absolutePath}")
        if (isNew) {
            AppDatabase.Schema.create(driver)
        }
        return driver
    }
}
