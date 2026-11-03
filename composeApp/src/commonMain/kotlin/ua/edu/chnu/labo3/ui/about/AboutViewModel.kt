package ua.edu.chnu.labo3.ui.about

import ua.edu.chnu.labo3.data.SystemInfo
import ua.edu.chnu.labo3.domain.usecase.GetSystemInfoUseCase
import ua.edu.chnu.labo3.domain.usecase.RegisterAboutVisitUseCase
import ua.edu.chnu.labo3.ui.mvi.MviViewModel
import ua.edu.chnu.labo3.ui.mvi.UiIntent
import ua.edu.chnu.labo3.ui.mvi.UiState
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

/** MVI Model of the About (system info) screen. */
data class AboutUiState(
    val isLoaded: Boolean = false,
    val visit: AboutVisitUiState = AboutVisitUiState(openCount = 0, lastOpenedText = ""),
    val items: List<AboutItem> = emptyList(),
) : UiState

sealed interface AboutIntent : UiIntent {
    /** The screen was opened: collect system info and record the visit. */
    data object Load : AboutIntent
}

/**
 * System data comes from [GetSystemInfoUseCase]; the persistent open counter and
 * last-open timestamp come from [RegisterAboutVisitUseCase]. The ViewModel only
 * turns them into a ready-to-render [AboutUiState].
 */
class AboutViewModel(
    private val getSystemInfo: GetSystemInfoUseCase,
    private val registerAboutVisit: RegisterAboutVisitUseCase,
) : MviViewModel<AboutUiState, AboutIntent>(AboutUiState()) {

    // Counted once per ViewModel (not on every recomposition / rotation).
    init {
        onIntent(AboutIntent.Load)
    }

    override fun handleIntent(intent: AboutIntent) = when (intent) {
        AboutIntent.Load -> load()
    }

    private fun load() {
        val info = getSystemInfo()
        val stats = registerAboutVisit()
        setState {
            copy(
                isLoaded = true,
                visit = AboutVisitUiState(
                    openCount = stats.openCount,
                    lastOpenedText = stats.previousOpenEpochMillis
                        ?.let { formatEpochMillis(it) }
                        ?: "This is your first visit",
                ),
                items = buildItems(info),
            )
        }
    }

    private fun buildItems(info: SystemInfo): List<AboutItem> {
        val result = mutableListOf(
            AboutItem("Operating System", "${info.osName} ${info.osVersion}"),
            AboutItem("Device", info.deviceModel),
            AboutItem("CPU", info.cpuType),
        )

        val max = max(info.screenWidth, info.screenHeight)
        val min = min(info.screenWidth, info.screenHeight)

        var displayInfo = "${max}×${min}"
        info.screenDensity?.let { displayInfo += " @${it}x" }
        result.add(AboutItem("Display", displayInfo))

        return result
    }
}
