package ua.edu.chnu.labo3.ui.puttext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.domain.usecase.UpdatePostTextUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** MVI Model of the Lab 14 "PUT as plain text" screen. */
data class PutTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
) : UiState

sealed interface PutTextIntent : UiIntent {
    /** Perform PUT /posts/{id}. */
    data object Send : PutTextIntent
}

class PutTextViewModel(
    private val updatePostText: UpdatePostTextUseCase,
) : MviViewModel<PutTextUiState, PutTextIntent>(PutTextUiState()) {

    override fun handleIntent(intent: PutTextIntent) = when (intent) {
        PutTextIntent.Send -> send()
    }

    private fun send() {
        viewModelScope.launch {
            setState { copy(isLoading = true, text = "", errorText = "") }
            val result = updatePostText(
                id = 1,
                title = "Lab 14 cross-platform",
                body = "Updated from the PUT as text screen.",
            )
            when (result) {
                is NetworkResult.Success -> setState { copy(isLoading = false, text = result.data) }
                is NetworkResult.Error -> setState { copy(isLoading = false, errorText = result.message) }
            }
        }
    }
}
