package ua.edu.chnu.labo3.ui.posttext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.PostRepository

/** UI state for the Lab 13 "POST as plain text" screen. */
data class PostTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
)

/**
 * Lab 13 ViewModel: sends a single POST request and exposes the response body
 * as plain text. It reuses [PostRepository], which wraps every call in a
 * [NetworkResult] so success and failure are handled uniformly.
 */
class PostTextViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PostTextUiState())
    val state: StateFlow<PostTextUiState> = _state.asStateFlow()

    /** Performs POST /posts and stores the raw response text in the UI state. */
    fun send() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, text = "", errorText = "") }
            val result = repository.createPostText(
                title = "Lab 13 cross-platform",
                body = "Created from the POST as text screen.",
            )
            when (result) {
                is NetworkResult.Success ->
                    _state.update { it.copy(isLoading = false, text = result.data) }
                is NetworkResult.Error ->
                    _state.update { it.copy(isLoading = false, errorText = result.message) }
            }
        }
    }
}
