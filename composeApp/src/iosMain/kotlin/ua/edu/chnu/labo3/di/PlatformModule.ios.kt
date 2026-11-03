package ua.edu.chnu.labo3.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ua.edu.chnu.labo3.data.DatabaseDriverFactory

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
}
