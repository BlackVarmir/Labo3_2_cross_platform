package ua.edu.chnu.labo3.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Shared Koin entry point.
 *
 * Each platform calls [initKoin] from its own entry point (Android [android.app.Application],
 * the JVM `main` function, the iOS view controller). The optional [appDeclaration]
 * lets a platform add extra configuration, e.g. `androidContext(...)` / `androidLogger()`.
 *
 * Both the shared [appModule] and the per-target [platformModule] are loaded.
 */
fun initKoin(appDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        appDeclaration?.invoke(this)
        modules(appModule, platformModule)
    }
}
