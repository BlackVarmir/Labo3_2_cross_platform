package ua.edu.chnu.labo3.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import ua.edu.chnu.labo3.util.nowEpochMillis

/**
 * A single reminder shown in the Reminders list.
 *
 * @param id unique row id (assigned by the database).
 * @param title short reminder text (required).
 * @param note optional longer description.
 * @param isDone whether the reminder has been completed.
 * @param createdAt epoch-millis moment the reminder was created.
 */
data class Reminder(
    val id: Long,
    val title: String,
    val note: String,
    val isDone: Boolean,
    val createdAt: Long,
)

/**
 * Single source of truth for the reminders list.
 *
 * The ViewModel depends on this abstraction, not on a concrete data source, so
 * the implementation can be swapped (e.g. from the in-memory version used while
 * prototyping the screen to the SQLDelight-backed one) or faked in tests.
 */
interface ReminderRepository {
    /** Stream of all reminders; emits a new list whenever the data changes. */
    fun observeAll(): Flow<List<Reminder>>

    /** Adds a new (not-done) reminder with the current time as its creation moment. */
    fun add(title: String, note: String)

    /** Marks the reminder with [id] as done/not-done. */
    fun setDone(id: Long, isDone: Boolean)

    /** Removes the reminder with [id]. */
    fun delete(id: Long)

    /** Removes every reminder. */
    fun clear()
}

/**
 * In-memory [ReminderRepository] used to build and validate the screen before
 * the database is wired in. Data lives only for the current app session.
 */
class InMemoryReminderRepository : ReminderRepository {

    private val state = MutableStateFlow<List<Reminder>>(emptyList())
    private var nextId = 1L

    override fun observeAll(): Flow<List<Reminder>> = state

    override fun add(title: String, note: String) = state.update { list ->
        list + Reminder(
            id = nextId++,
            title = title,
            note = note,
            isDone = false,
            createdAt = nowEpochMillis(),
        )
    }

    override fun setDone(id: Long, isDone: Boolean) = state.update { list ->
        list.map { if (it.id == id) it.copy(isDone = isDone) else it }
    }

    override fun delete(id: Long) = state.update { list ->
        list.filterNot { it.id == id }
    }

    override fun clear() = state.update { emptyList() }
}
