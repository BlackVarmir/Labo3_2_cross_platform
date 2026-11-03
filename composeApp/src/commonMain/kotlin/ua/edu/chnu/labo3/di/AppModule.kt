package ua.edu.chnu.labo3.di

import com.russhwolf.settings.Settings
import org.koin.core.module.dsl.factoryOf
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
import ua.edu.chnu.labo3.domain.usecase.AddReminderUseCase
import ua.edu.chnu.labo3.domain.usecase.CreatePostTextUseCase
import ua.edu.chnu.labo3.domain.usecase.CreatePostUseCase
import ua.edu.chnu.labo3.domain.usecase.DeletePostTextUseCase
import ua.edu.chnu.labo3.domain.usecase.DeletePostUseCase
import ua.edu.chnu.labo3.domain.usecase.DeleteReminderUseCase
import ua.edu.chnu.labo3.domain.usecase.GetPostUseCase
import ua.edu.chnu.labo3.domain.usecase.GetPostsTextUseCase
import ua.edu.chnu.labo3.domain.usecase.GetSystemInfoUseCase
import ua.edu.chnu.labo3.domain.usecase.ObserveRemindersUseCase
import ua.edu.chnu.labo3.domain.usecase.RegisterAboutVisitUseCase
import ua.edu.chnu.labo3.domain.usecase.ToggleReminderDoneUseCase
import ua.edu.chnu.labo3.domain.usecase.UpdatePostTextUseCase
import ua.edu.chnu.labo3.domain.usecase.UpdatePostUseCase
import ua.edu.chnu.labo3.ui.about.AboutViewModel
import ua.edu.chnu.labo3.ui.deletetext.DeleteTextViewModel
import ua.edu.chnu.labo3.ui.gettext.GetTextViewModel
import ua.edu.chnu.labo3.ui.network.NetworkViewModel
import ua.edu.chnu.labo3.ui.posttext.PostTextViewModel
import ua.edu.chnu.labo3.ui.puttext.PutTextViewModel
import ua.edu.chnu.labo3.ui.reminders.RemindersViewModel

/**
 * The shared application dependency graph (Lab 3: Data -> Domain -> Presentation).
 *
 *   Platform              ->  SystemInfoRepository   -> GetSystemInfoUseCase     --\
 *                                                                              >--> AboutViewModel -> UI
 *   Settings              ->  AppSettingsRepository  -> RegisterAboutVisitUseCase --/
 *
 *   DatabaseDriverFactory ->  SqlDriver -> AppDatabase -> ReminderRepository
 *                                  -> Observe/Add/Toggle/DeleteReminderUseCase -> RemindersViewModel -> UI
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

    // Domain layer: use cases (a fresh, stateless instance per injection).
    factoryOf(::GetPostUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::UpdatePostUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::GetPostsTextUseCase)
    factoryOf(::CreatePostTextUseCase)
    factoryOf(::UpdatePostTextUseCase)
    factoryOf(::DeletePostTextUseCase)
    factoryOf(::ObserveRemindersUseCase)
    factoryOf(::AddReminderUseCase)
    factoryOf(::ToggleReminderDoneUseCase)
    factoryOf(::DeleteReminderUseCase)
    factoryOf(::GetSystemInfoUseCase)
    factoryOf(::RegisterAboutVisitUseCase)

    // MVI ViewModels; they depend only on use cases.
    viewModelOf(::AboutViewModel)
    viewModelOf(::RemindersViewModel)
    viewModelOf(::NetworkViewModel)
    viewModelOf(::GetTextViewModel)
    viewModelOf(::PostTextViewModel)
    viewModelOf(::PutTextViewModel)
    viewModelOf(::DeleteTextViewModel)
}
