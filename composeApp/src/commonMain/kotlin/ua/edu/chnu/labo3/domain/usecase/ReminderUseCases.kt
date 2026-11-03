package ua.edu.chnu.labo3.domain.usecase

import kotlinx.coroutines.flow.Flow
import ua.edu.chnu.labo3.data.Reminder
import ua.edu.chnu.labo3.data.ReminderRepository

/** Stream of all reminders. */
class ObserveRemindersUseCase(private val repository: ReminderRepository) {
    operator fun invoke(): Flow<List<Reminder>> = repository.observeAll()
}

/**
 * Adds a reminder. The business rule "a title must not be blank, input is
 * trimmed" lives here in the domain layer, not in the ViewModel.
 *
 * @return `true` if the reminder was added, `false` if the title was blank.
 */
class AddReminderUseCase(private val repository: ReminderRepository) {
    operator fun invoke(title: String, note: String): Boolean {
        if (title.isBlank()) return false
        repository.add(title.trim(), note.trim())
        return true
    }
}

/** Flips the done flag of a reminder. */
class ToggleReminderDoneUseCase(private val repository: ReminderRepository) {
    operator fun invoke(reminder: Reminder) = repository.setDone(reminder.id, !reminder.isDone)
}

/** Deletes a reminder. */
class DeleteReminderUseCase(private val repository: ReminderRepository) {
    operator fun invoke(reminder: Reminder) = repository.delete(reminder.id)
}
