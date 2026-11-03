package ua.edu.chnu.labo3.di

import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ua.edu.chnu.labo3.Platform
import ua.edu.chnu.labo3.data.ApiService
import ua.edu.chnu.labo3.data.ApiServiceImpl
import ua.edu.chnu.labo3.data.AppSettingsRepository
import ua.edu.chnu.labo3.data.DatabaseDriverFactory
import ua.edu.chnu.labo3.data.PlatformSystemInfoRepository
import ua.edu.chnu.labo3.data.PostRepository
import ua.edu.chnu.labo3.data.PostRepositoryImpl
import ua.edu.chnu.labo3.data.ReminderRepository
import ua.edu.chnu.labo3.data.SettingsAppSettingsRepository
import ua.edu.chnu.labo3.data.SqlDelightReminderRepository
import ua.edu.chnu.labo3.data.SystemInfoRepository
import ua.edu.chnu.labo3.data.createHttpClient
import ua.edu.chnu.labo3.db.AppDatabase
import ua.edu.chnu.labo3.ui.about.AboutViewModel
import ua.edu.chnu.labo3.ui.deletetext.DeleteTextViewModel
import ua.edu.chnu.labo3.ui.gettext.GetTextViewModel
import ua.edu.chnu.labo3.ui.network.NetworkViewModel
import ua.edu.chnu.labo3.ui.posttext.PostTextViewModel
import ua.edu.chnu.labo3.ui.puttext.PutTextViewModel
import ua.edu.chnu.labo3.ui.reminders.RemindersViewModel

/**
 * The shared application dependency graph.
 *
 *   Platform              ->  SystemInfoRepository   --\
 *                                                       >--> AboutViewModel    -> UI
 *   Settings              ->  AppSettingsRepository  --/
 *
 *   DatabaseDriverFactory ->  SqlDriver -> AppDatabase -> ReminderRepository
 *                                                              -> RemindersViewModel -> UI
 *
 * The [DatabaseDriverFactory] itself is provided by [platformModule], because
 * its constructor is platform-specific. Koin resolves the rest on demand.
 */
val appModule = module {
    // Platform-specific (expect/actual) system data source.
    single { Platform() }

    // Repository that supplies system information, backed by Platform.
    single<SystemInfoRepository> { PlatformSystemInfoRepository(get()) }

    // Persistent key-value store (Multiplatform Settings, no-arg factory).
    single<Settings> { Settings() }

    // Repository that supplies persistent app state (About-screen visit stats).
    single<AppSettingsRepository> { SettingsAppSettingsRepository(get()) }

    // SQLDelight database: build the driver via the platform factory, then the DB.
    single { get<DatabaseDriverFactory>().createDriver() }
    single { AppDatabase(get()) }

    // Reminders repository, now backed by the database.
    single<ReminderRepository> { SqlDelightReminderRepository(get()) }

    // Networking graph: HttpClient -> ApiService -> PostRepository -> NetworkViewModel.
    single { createHttpClient() }
    single<ApiService> { ApiServiceImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get()) }

    // ViewModels; their dependencies are injected.
    viewModelOf(::AboutViewModel)
    viewModelOf(::RemindersViewModel)
    viewModelOf(::NetworkViewModel)
    viewModelOf(::GetTextViewModel)
    viewModelOf(::PostTextViewModel)
    viewModelOf(::PutTextViewModel)
    viewModelOf(::DeleteTextViewModel)
}
