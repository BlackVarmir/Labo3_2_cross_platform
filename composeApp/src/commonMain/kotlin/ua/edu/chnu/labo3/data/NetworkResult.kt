package ua.edu.chnu.labo3.data

/**
 * Helper type representing the outcome of an API call.
 *
 * Instead of throwing, the repository wraps every call in a [NetworkResult],
 * so the presentation layer can render success and failure uniformly without
 * try/catch blocks.
 */
sealed interface NetworkResult<out T> {
    /** The call succeeded and produced [data]. */
    data class Success<out T>(val data: T) : NetworkResult<T>

    /** The call failed with a human-readable [message] (and optional [cause]). */
    data class Error(val message: String, val cause: Throwable? = null) : NetworkResult<Nothing>
}

/**
 * Runs [block] and wraps the outcome in a [NetworkResult], converting any
 * thrown exception into [NetworkResult.Error].
 */
suspend fun <T> networkResultOf(block: suspend () -> T): NetworkResult<T> =
    try {
        NetworkResult.Success(block())
    } catch (e: Throwable) {
        NetworkResult.Error(e.message ?: "Unknown network error", e)
    }
