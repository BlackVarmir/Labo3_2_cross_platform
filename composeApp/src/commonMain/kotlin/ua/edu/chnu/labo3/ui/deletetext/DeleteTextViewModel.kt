package ua.edu.chnu.labo3.ui.deletetext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.Post
import ua.edu.chnu.labo3.data.PostRepository

/** UI state for the Lab 15 "DELETE as plain text" screen. */
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
)

/**
 * Lab 15 ViewModel.
 *
 * 1. [loadPost] fetches the post with GET, so the screen can show what will be deleted.
 * 2. [delete] runs only when the user presses the button: it sends DELETE /posts/{id}
 *    and exposes the server response (HTTP status + body) as plain text.
 */
class DeleteTextViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DeleteTextUiState())
    val state: StateFlow<DeleteTextUiState> = _state.asStateFlow()

    // Show the post as soon as the screen opens; nothing is deleted automatically.
    init {
        loadPost()
    }

    /** GET /posts/{id}: shows the post before it is deleted. Also resets the screen. */
    fun loadPost() {
        viewModelScope.launch {
            _state.value = DeleteTextUiState(isLoadingPost = true)
            when (val result = repository.getPost(POST_ID)) {
                is NetworkResult.Success ->
                    _state.update { it.copy(isLoadingPost = false, post = result.data) }
                is NetworkResult.Error ->
                    _state.update { it.copy(isLoadingPost = false, postError = result.message) }
            }
        }
    }

    /** DELETE /posts/{id}: deletes the post and stores the raw response as plain text. */
    fun delete() {
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true, resultText = "", deleteError = "") }
            when (val result = repository.deletePostText(POST_ID)) {
                is NetworkResult.Success -> _state.update {
                    it.copy(
                        isDeleting = false,
                        isDeleted = true,
                        resultText = buildString {
                            appendLine("Request:  DELETE /posts/$POST_ID")
                            appendLine("Response: ${result.data.status}")
                            appendLine()
                            appendLine("Response body:")
                            append(result.data.body)
                        },
                    )
                }
                is NetworkResult.Error ->
                    _state.update { it.copy(isDeleting = false, deleteError = result.message) }
            }
        }
    }

    companion object {
        /** Id of the post deleted by this screen. */
        const val POST_ID = 1
    }
}
