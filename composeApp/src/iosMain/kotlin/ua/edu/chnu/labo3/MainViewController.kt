package ua.edu.chnu.labo3

import androidx.compose.ui.window.ComposeUIViewController
import org.koin.core.context.GlobalContext
import ua.edu.chnu.labo3.di.initKoin

/**
 * iOS entry point used from Swift, e.g.:
 *
 * ```swift
 * @main
 * struct iOSApp: App {
 *     init() { KoinKt.doInitKoin() }
 *     ...
 * }
 * ```
 */
fun doInitKoin() = initKoin()

fun MainViewController() = ComposeUIViewController {
    // If Swift didn't start Koin yet, start it here so the framework is self-sufficient.
    if (GlobalContext.getOrNull() == null) {
        initKoin()
    }
    App()
}
