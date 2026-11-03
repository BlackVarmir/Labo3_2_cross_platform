package ua.edu.chnu.labo3.ui.network

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.edu.chnu.labo3.data.NetworkResult
import ua.edu.chnu.labo3.data.map
import ua.edu.chnu.labo3.domain.usecase.CreatePostUseCase
import ua.edu.chnu.labo3.domain.usecase.DeletePostUseCase
import ua.edu.chnu.labo3.domain.usecase.GetPostUseCase
import ua.edu.chnu.labo3.domain.usecase.UpdatePostUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState

/** MVI Model of the networking test screen. */
data class NetworkUiState(
    val isLoading: Boolean = false,
    val successText: String = "",
    val errorText: String = "",
) : UiState

/** The four buttons of the screen. */
sealed interface NetworkIntent : UiIntent {
    data object Get : NetworkIntent
    data object Post : NetworkIntent
    data object Put : NetworkIntent
    data object Delete : NetworkIntent
}

/**
 * Each intent runs the matching use case, shows a progress flag while in
 * flight, and routes the outcome to the success or the error text of the state.
 */
class NetworkViewModel(
    private val getPost: GetPostUseCase,
    private val createPost: CreatePostUseCase,
    private val updatePost: UpdatePostUseCase,
    private val deletePost: DeletePostUseCase,
) : MviViewModel<NetworkUiState, NetworkIntent>(NetworkUiState()) {

    /** A fixed sample id used by GET / PUT / DELETE against the test API. */
    private val sampleId = 1

    override fun handleIntent(intent: NetworkIntent) = when (intent) {
        NetworkIntent.Get -> run("GET /posts/$sampleId") { getPost(sampleId) }
        NetworkIntent.Post -> run("POST /posts") {
            createPost(title = "labo3 post", body = "Created from the KMP app.")
        }
        NetworkIntent.Put -> run("PUT /posts/$sampleId") {
            updatePost(id = sampleId, title = "labo3 updated", body = "Updated from the KMP app.")
        }
        NetworkIntent.Delete -> run("DELETE /posts/$sampleId") {
            deletePost(sampleId).map { "Post #$sampleId deleted." }
        }
    }

    /** Shared launch/loading/result plumbing for every verb. */
    private fun run(label: String, call: suspend () -> NetworkResult<Any?>) {
        viewModelScope.launch {
            setState { copy(isLoading = true, successText = "", errorText = "") }
            when (val result = call()) {
                is NetworkResult.Success ->
                    setState { copy(isLoading = false, successText = "$label ->\n${result.data}") }
                is NetworkResult.Error ->
                    setState { copy(isLoading = false, errorText = "$label failed:\n${result.message}") }
            }
        }
    }
}
