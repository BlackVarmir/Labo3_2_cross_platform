package ua.edu.chnu.labo3.ui.puttext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.PostRepository

/** UI state for the Lab 14 "PUT as plain text" screen. */
data class PutTextUiState(
    val isLoading: Boolean = false,
    val text: String = "",
    val errorText: String = "",
)

/**
 * Lab 14 ViewModel: sends a single PUT request and exposes the response body
 * as plain text. It reuses [PostRepository], which wraps every call in a
 * [NetworkResult] so success and failure are handled uniformly.
 */
class PutTextViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PutTextUiState())
    val state: StateFlow<PutTextUiState> = _state.asStateFlow()

    /** Performs PUT /posts/{id} and stores the raw response text in the UI state. */
    fun send() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, text = "", errorText = "") }
            val result = repository.updatePostText(
                id = 1,
                title = "Lab 14 cross-platform",
                body = "Updated from the PUT as text screen.",
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
