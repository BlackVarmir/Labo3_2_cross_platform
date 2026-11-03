package ua.edu.chnu.labo3

/**
 * Platform-specific API.
 *
 * The shared code declares WHAT information it needs about the running system,
 * while each target (Android, iOS, Desktop/JVM) provides the actual
 * implementation using its own platform APIs.
 */
expect class Platform() {
    val osName: String
    val osVersion: String

    val deviceModel: String
    val cpuType: String

    val screen: ScreenInfo

    /** Logs the collected system information using the logging library. */
    fun logSystemInfo()
}

expect class ScreenInfo() {
    val width: Int
    val height: Int
    val density: Int?
}

/** Human-readable one-line summary of the device, e.g. "(Android; 34; ...)". */
val Platform.deviceInfo: String
    get() {
        var result = "($osName; $osVersion; $deviceModel; ${screen.width}x${screen.height}"

        screen.density?.let {
            result += "@${it}x"
        }

        result += "; $cpuType)"
        return result
    }
