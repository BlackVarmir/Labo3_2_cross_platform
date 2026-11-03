package ua.edu.chnu.labo3.data

import com.russhwolf.settings.Settings
import ua.edu.chnu.labo3.util.nowEpochMillis

/**
 * Snapshot of the "About" screen visit statistics that the UI needs.
 *
 * @param openCount how many times the About screen has been opened (including the current visit).
 * @param previousOpenEpochMillis when the About screen was opened the time before this one,
 *        or `null` if this is the very first visit.
 */
data class AboutVisitInfo(
    val openCount: Int,
    val previousOpenEpochMillis: Long?,
)

/**
 * Stores and supplies small pieces of persistent application state.
 *
 * Backed by the Multiplatform Settings library so the data survives app
 * restarts on every target (SharedPreferences on Android, Preferences on the
 * JVM, NSUserDefaults on iOS). The UI/ViewModel layer depends on this
 * abstraction, not on [Settings] directly, so it can be faked in tests.
 */
interface AppSettingsRepository {

    /**
     * Records that the About screen has just been opened: increments the open
     * counter and stores the current time as the last-open moment. Returns the
     * updated statistics (the new count and the *previous* open time).
     */
    fun registerAboutOpened(): AboutVisitInfo

    /** Returns the current statistics without recording a new visit. */
    fun getAboutVisitInfo(): AboutVisitInfo
}

/**
 * Default [AppSettingsRepository] backed by Multiplatform [Settings].
 */
class SettingsAppSettingsRepository(
    private val settings: Settings,
) : AppSettingsRepository {

    override fun registerAboutOpened(): AboutVisitInfo {
        val newCount = settings.getInt(KEY_OPEN_COUNT, 0) + 1
        val previousOpen =
            if (settings.hasKey(KEY_LAST_OPENED)) settings.getLong(KEY_LAST_OPENED, 0L) else null

        settings.putInt(KEY_OPEN_COUNT, newCount)
        settings.putLong(KEY_LAST_OPENED, nowEpochMillis())

        return AboutVisitInfo(openCount = newCount, previousOpenEpochMillis = previousOpen)
    }

    override fun getAboutVisitInfo(): AboutVisitInfo {
        val count = settings.getInt(KEY_OPEN_COUNT, 0)
        val lastOpened =
            if (settings.hasKey(KEY_LAST_OPENED)) settings.getLong(KEY_LAST_OPENED, 0L) else null
        return AboutVisitInfo(openCount = count, previousOpenEpochMillis = lastOpened)
    }

    private companion object {
        const val KEY_OPEN_COUNT = "about_open_count"
        const val KEY_LAST_OPENED = "about_last_opened"
    }
}
