package ua.edu.chnu.labo3.ui.posttext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.domain.usecase.CreatePostTextUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** MVI Model of the Lab 13 "POST as plain text" screen. */
data class PostTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
) : UiState

sealed interface PostTextIntent : UiIntent {
    /** Perform POST /posts. */
    data object Send : PostTextIntent
}

class PostTextViewModel(
    private val createPostText: CreatePostTextUseCase,
) : MviViewModel<PostTextUiState, PostTextIntent>(PostTextUiState()) {

    override fun handleIntent(intent: PostTextIntent) = when (intent) {
        PostTextIntent.Send -> send()
    }

    private fun send() {
        viewModelScope.launch {
            setState { copy(isLoading = true, text = "", errorText = "") }
            val result = createPostText(
                title = "Lab 13 cross-platform",
                body = "Created from the POST as text screen.",
            )
            when (result) {
                is NetworkResult.Success -> setState { copy(isLoading = false, text = result.data) }
                is NetworkResult.Error -> setState { copy(isLoading = false, errorText = result.message) }
            }
        }
    }
}
