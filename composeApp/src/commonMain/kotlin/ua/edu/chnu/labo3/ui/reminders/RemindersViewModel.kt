package ua.edu.chnu.labo3.ui.reminders

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.edu.chnu.labo3.data.Reminder
import ua.edu.chnu.labo3.domain.usecase.AddReminderUseCase
import ua.edu.chnu.labo3.domain.usecase.DeleteReminderUseCase
import ua.edu.chnu.labo3.domain.usecase.ObserveRemindersUseCase
import ua.edu.chnu.labo3.domain.usecase.ToggleReminderDoneUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** MVI Model of the Reminders screen. */
data class RemindersUiState(
    val reminders: List<Reminder> = emptyList(),
) : UiState

sealed interface RemindersIntent : UiIntent {
    data class Add(val title: String, val note: String) : RemindersIntent
    data class ToggleDone(val reminder: Reminder) : RemindersIntent
    data class Delete(val reminder: Reminder) : RemindersIntent
}

/**
 * The reminders list is a stream: the use case's [kotlinx.coroutines.flow.Flow]
 * is folded into the single [RemindersUiState] for as long as the ViewModel
 * lives. User actions arrive as [RemindersIntent]s and go to the use cases;
 * the database emits the new list, which becomes the new state.
 */
class RemindersViewModel(
    observeReminders: ObserveRemindersUseCase,
    private val addReminder: AddReminderUseCase,
    private val toggleReminderDone: ToggleReminderDoneUseCase,
    private val deleteReminder: DeleteReminderUseCase,
) : MviViewModel<RemindersUiState, RemindersIntent>(RemindersUiState()) {

    init {
        observeReminders()
            .onEach { list -> setState { copy(reminders = list) } }
            .launchIn(viewModelScope)
    }

    override fun handleIntent(intent: RemindersIntent) {
        when (intent) {
            is RemindersIntent.Add -> addReminder(intent.title, intent.note)
            is RemindersIntent.ToggleDone -> toggleReminderDone(intent.reminder)
            is RemindersIntent.Delete -> deleteReminder(intent.reminder)
        }
    }
}
