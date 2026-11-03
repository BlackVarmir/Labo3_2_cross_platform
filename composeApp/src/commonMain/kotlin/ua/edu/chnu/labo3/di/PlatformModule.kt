package ua.edu.chnu.labo3.di

import org.koin.core.module.Module

/**
 * Platform-specific Koin module.
 *
 * Each target supplies the [ua.edu.chnu.labo3.data.DatabaseDriverFactory],
 * whose constructor differs per platform (Android needs a `Context`, the JVM
 * and iOS do not). Keeping it here lets [appModule] stay fully shared.
 */
expect val platformModule: Module
