package ua.edu.chnu.labo3.ui.about

import androidx.lifecycle.ViewModel
import ua.edu.chnu.labo3.data.AppSettingsRepository
import ua.edu.chnu.labo3.data.SystemInfoRepository
import ua.edu.chnu.labo3.util.formatEpochMillis
import kotlin.math.max
import kotlin.math.min

/** A single title/value row shown on the About screen. */
data class AboutItem(
    val title: String,
    val value: String,
)

/** Visit statistics shown at the top of the About screen. */
data class AboutVisitUiState(
    val openCount: Int,
    val lastOpenedText: String,
)

/**
 * ViewModel for the About (system info) screen.
 *
 * System data comes from [SystemInfoRepository]; the persistent open-counter and
 * last-open timestamp come from [AppSettingsRepository]. Both are injected by
 * Koin, so the ViewModel only records the visit and formats data for the UI.
 */
class AboutViewModel(
    private val repository: SystemInfoRepository,
    private val settingsRepository: AppSettingsRepository,
) : ViewModel() {

    // Collect system info once via the repository and log it.
    private val info = repository.getSystemInfo().also { repository.logSystemInfo() }

    // Record this visit to the About screen and expose the resulting statistics.
    val visit: AboutVisitUiState = settingsRepository.registerAboutOpened().let { stats ->
        AboutVisitUiState(
            openCount = stats.openCount,
            lastOpenedText = stats.previousOpenEpochMillis
                ?.let { formatEpochMillis(it) }
                ?: "This is your first visit",
        )
    }

    /** Ready-to-render rows for the About screen. */
    val items: List<AboutItem> = buildItems()

    private fun buildItems(): List<AboutItem> {
        val result = mutableListOf(
            AboutItem("Operating System", "${info.osName} ${info.osVersion}"),
            AboutItem("Device", info.deviceModel),
            AboutItem("CPU", info.cpuType),
        )

        val max = max(info.screenWidth, info.screenHeight)
        val min = min(info.screenWidth, info.screenHeight)

        var displayInfo = "${max}×${min}"
        info.screenDensity?.let {
            displayInfo += " @${it}x"
        }
        result.add(AboutItem("Display", displayInfo))

        return result
    }
}
