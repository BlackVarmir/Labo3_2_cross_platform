package ua.edu.chnu.labo3.ui.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.map
import ua.edu.chnu.labo3.data.PostRepository

/** UI state for the networking test screen. */
data class NetworkUiState(
    val isLoading: Boolean = false,
    val successText: String = "",
    val errorText: String = "",
)

/**
 * ViewModel connecting the presentation layer to [PostRepository].
 *
 * Each of the four actions (GET / POST / PUT / DELETE) runs the matching call,
 * shows a progress flag while in flight, and routes the outcome to either the
 * success or the error text of [NetworkUiState].
 */
class NetworkViewModel(
    private val repository: PostRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(NetworkUiState())
    val state: StateFlow<NetworkUiState> = _state.asStateFlow()

    /** A fixed sample id used by GET / PUT / DELETE against the test API. */
    private val sampleId = 1

    fun get() = run("GET /posts/$sampleId") { repository.getPost(sampleId) }

    fun post() = run("POST /posts") {
        repository.createPost(title = "labo3 post", body = "Created from the KMP app.")
    }

    fun put() = run("PUT /posts/$sampleId") {
        repository.updatePost(id = sampleId, title = "labo3 updated", body = "Updated from the KMP app.")
    }

    fun delete() = run("DELETE /posts/$sampleId") {
        repository.deletePost(sampleId).map { "Post #$sampleId deleted." }
    }

    /** Shared launch/loading/result plumbing for every verb. */
    private fun run(label: String, call: suspend () -> NetworkResult<Any?>) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, successText = "", errorText = "") }
            val result = call()
            _state.update {
                when (result) {
                    is NetworkResult.Success ->
                        it.copy(isLoading = false, successText = "$label ->\n${result.data}")
                    is NetworkResult.Error ->
                        it.copy(isLoading = false, errorText = "$label failed:\n${result.message}")
                }
            }
        }
    }
}
