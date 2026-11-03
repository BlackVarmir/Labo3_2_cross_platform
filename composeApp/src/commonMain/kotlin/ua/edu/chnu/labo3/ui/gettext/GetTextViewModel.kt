package ua.edu.chnu.labo3.ui.gettext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.domain.usecase.GetPostsTextUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** MVI Model of the Lab 12 "GET as plain text" screen. */
data class GetTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
) : UiState

/** Everything the user can do on the screen. */
sealed interface GetTextIntent : UiIntent {
    /** Perform GET /posts. */
    data object Load : GetTextIntent
}

/** MVI ViewModel: Intent -> use case -> new [GetTextUiState]. */
class GetTextViewModel(
    private val getPostsText: GetPostsTextUseCase,
) : MviViewModel<GetTextUiState, GetTextIntent>(GetTextUiState()) {

    override fun handleIntent(intent: GetTextIntent) = when (intent) {
        GetTextIntent.Load -> load()
    }

    private fun load() {
        viewModelScope.launch {
            setState { copy(isLoading = true, text = "", errorText = "") }
            when (val result = getPostsText()) {
                is NetworkResult.Success -> setState { copy(isLoading = false, text = result.data) }
                is NetworkResult.Error -> setState { copy(isLoading = false, errorText = result.message) }
            }
        }
    }
}
