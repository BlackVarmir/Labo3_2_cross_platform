package ua.edu.chnu.labo3.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import ua.edu.chnu.labo3.db.AppDatabase
import ua.edu.chnu.labo3.util.nowEpochMillis

/**
 * [ReminderRepository] backed by the SQLDelight database.
 *
 * Reads are exposed as a reactive [Flow] (via SQLDelight's coroutines
 * extensions), so the UI updates automatically after every insert/update/delete.
 * Writes go straight to the generated, type-safe queries.
 */
class SqlDelightReminderRepository(
    database: AppDatabase,
) : ReminderRepository {

    private val queries = database.reminderQueries

    override fun observeAll(): Flow<List<Reminder>> =
        queries.selectAll { id, title, note, isDone, createdAt ->
            Reminder(
                id = id,
                title = title,
                note = note,
                isDone = isDone,
                createdAt = createdAt,
            )
        }.asFlow().mapToList(Dispatchers.Default)

    override fun add(title: String, note: String) {
        queries.insert(
            title = title,
            note = note,
            isDone = false,
            createdAt = nowEpochMillis(),
        )
    }

    override fun setDone(id: Long, isDone: Boolean) {
        queries.setDone(isDone = isDone, id = id)
    }

    override fun delete(id: Long) {
        queries.deleteById(id)
    }

    override fun clear() {
        queries.deleteAll()
    }
}
