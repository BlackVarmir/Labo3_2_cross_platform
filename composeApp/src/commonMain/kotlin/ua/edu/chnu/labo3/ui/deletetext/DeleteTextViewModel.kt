package ua.edu.chnu.labo3.ui.deletetext

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.Post
import ua.edu.chnu.labo3.domain.usecase.DeletePostTextUseCase
import ua.edu.chnu.labo3.domain.usecase.GetPostUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** Id of the post deleted by this screen. */
const val DELETE_POST_ID = 1

/** MVI Model of the "DELETE as plain text" screen. */
data class DeleteTextUiState(
    /** The post that is about to be deleted (loaded with GET first). */
    val isLoadingPost: Boolean = false,
    val post: Post? = null,
    val postError: String = "",
    /** The DELETE call itself. */
    val isDeleting: Boolean = false,
    val isDeleted: Boolean = false,
    val resultText: String = "",
    val deleteError: String = "",
) : UiState

sealed interface DeleteTextIntent : UiIntent {
    /** GET /posts/{id}: show the post before it is deleted (also "Start over"). */
    data object LoadPost : DeleteTextIntent

    /** DELETE /posts/{id}: runs only when the user presses the button. */
    data object Delete : DeleteTextIntent
}

class DeleteTextViewModel(
    private val getPost: GetPostUseCase,
    private val deletePostText: DeletePostTextUseCase,
) : MviViewModel<DeleteTextUiState, DeleteTextIntent>(DeleteTextUiState()) {

    // Show the post as soon as the screen opens; nothing is deleted automatically.
    init {
        onIntent(DeleteTextIntent.LoadPost)
    }

    override fun handleIntent(intent: DeleteTextIntent) = when (intent) {
        DeleteTextIntent.LoadPost -> loadPost()
        DeleteTextIntent.Delete -> delete()
    }

    private fun loadPost() {
        viewModelScope.launch {
            setState { DeleteTextUiState(isLoadingPost = true) }
            when (val result = getPost(DELETE_POST_ID)) {
                is NetworkResult.Success -> setState { copy(isLoadingPost = false, post = result.data) }
                is NetworkResult.Error -> setState { copy(isLoadingPost = false, postError = result.message) }
            }
        }
    }

    private fun delete() {
        viewModelScope.launch {
            setState { copy(isDeleting = true, resultText = "", deleteError = "") }
            when (val result = deletePostText(DELETE_POST_ID)) {
                is NetworkResult.Success -> setState {
                    copy(
                        isDeleting = false,
                        isDeleted = true,
                        resultText = buildString {
                            appendLine("Request:  DELETE /posts/$DELETE_POST_ID")
                            appendLine("Response: ${result.data.status}")
                            appendLine()
                            appendLine("Response body:")
                            append(result.data.body)
                        },
                    )
                }
                is NetworkResult.Error -> setState { copy(isDeleting = false, deleteError = result.message) }
            }
        }
    }
}
