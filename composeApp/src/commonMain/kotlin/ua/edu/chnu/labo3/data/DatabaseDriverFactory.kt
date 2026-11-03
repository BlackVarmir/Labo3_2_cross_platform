package ua.edu.chnu.labo3.data

import app.cash.sqldelight.db.SqlDriver

/**
 * Creates the platform-specific [SqlDriver] used by SQLDelight.
 *
 * Each target supplies its own driver (Android SQLite, JVM JDBC SQLite, iOS
 * native SQLite). The shared code depends only on this expect declaration, so
 * the database layer above it stays completely platform-agnostic.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
