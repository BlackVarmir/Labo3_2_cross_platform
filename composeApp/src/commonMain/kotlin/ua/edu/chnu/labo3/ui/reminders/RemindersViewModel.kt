package ua.edu.chnu.labo3.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.Reminder
import ua.edu.chnu.labo3.data.ReminderRepository

/**
 * ViewModel for the Reminders screen.
 *
 * Exposes the reminders list as a [StateFlow] collected from the repository, and
 * forwards user actions (add / toggle done / delete) to it. The repository is
 * injected by Koin, so the ViewModel is unaware of where the data is stored.
 */
class RemindersViewModel(
    private val repository: ReminderRepository,
) : ViewModel() {

    /** The reactive reminders list, kept in sync with the database. */
    val reminders: StateFlow<List<Reminder>> =
        repository.observeAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    /** Adds a reminder if the title is not blank. */
    fun add(title: String, note: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.add(title.trim(), note.trim())
        }
    }

    /** Flips the done state of the given reminder. */
    fun toggleDone(reminder: Reminder) {
        viewModelScope.launch {
            repository.setDone(reminder.id, !reminder.isDone)
        }
    }

    /** Deletes the given reminder. */
    fun delete(reminder: Reminder) {
        viewModelScope.launch {
            repository.delete(reminder.id)
        }
    }
}
