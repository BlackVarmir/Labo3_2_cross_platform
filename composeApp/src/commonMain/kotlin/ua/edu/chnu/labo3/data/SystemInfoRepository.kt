package ua.edu.chnu.labo3.data

import ua.edu.chnu.labo3.Platform
import ua.edu.chnu.labo3.deviceInfo

/**
 * Plain, platform-agnostic snapshot of the system information that the UI needs.
 *
 * Keeping this as a simple data class means the UI/ViewModel layer never has to
 * touch the [Platform] expect/actual type directly.
 */
data class SystemInfo(
    val osName: String,
    val osVersion: String,
    val deviceModel: String,
    val cpuType: String,
    val screenWidth: Int,
    val screenHeight: Int,
    val screenDensity: Int?,
    val summary: String,
)

/**
 * Single source of truth for system information.
 *
 * The ViewModel depends on this abstraction (not on [Platform]), so the data
 * source can be swapped or faked. An instance is provided through Koin.
 */
interface SystemInfoRepository {
    /** Returns a ready-to-use snapshot of the current device/system info. */
    fun getSystemInfo(): SystemInfo

    /** Logs the collected system information using the logging library. */
    fun logSystemInfo()
}

/**
 * Default [SystemInfoRepository] backed by the platform-specific [Platform] API.
 */
class PlatformSystemInfoRepository(
    private val platform: Platform,
) : SystemInfoRepository {

    override fun getSystemInfo(): SystemInfo = SystemInfo(
        osName = platform.osName,
        osVersion = platform.osVersion,
        deviceModel = platform.deviceModel,
        cpuType = platform.cpuType,
        screenWidth = platform.screen.width,
        screenHeight = platform.screen.height,
        screenDensity = platform.screen.density,
        summary = platform.deviceInfo,
    )

    override fun logSystemInfo() = platform.logSystemInfo()
}
