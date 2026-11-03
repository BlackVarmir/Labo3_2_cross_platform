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

/**
 * Lab 3: the `map` extension for [NetworkResult].
 *
 * Transforms the payload of a [NetworkResult.Success] with [transform] and
 * leaves a [NetworkResult.Error] untouched, so a data-layer function can turn
 * the raw value coming from the service into the shape the rest of the app
 * needs without a `when` at every call site.
 *
 * If [transform] itself throws, the exception is converted into
 * [NetworkResult.Error] (same contract as [networkResultOf]).
 */
inline fun <T, R> NetworkResult<T>.map(transform: (T) -> R): NetworkResult<R> =
    when (this) {
        is NetworkResult.Success ->
            try {
                NetworkResult.Success(transform(data))
            } catch (e: Throwable) {
                NetworkResult.Error(e.message ?: "Mapping failed", e)
            }
        is NetworkResult.Error -> this
    }

/** Collapses a [NetworkResult] into a single value by handling both branches. */
inline fun <T, R> NetworkResult<T>.fold(
    onSuccess: (T) -> R,
    onError: (message: String) -> R,
): R = when (this) {
    is NetworkResult.Success -> onSuccess(data)
    is NetworkResult.Error -> onError(message)
}
