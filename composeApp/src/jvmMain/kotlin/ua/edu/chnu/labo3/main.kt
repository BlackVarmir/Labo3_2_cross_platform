package ua.edu.chnu.labo3

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ua.edu.chnu.labo3.di.initKoin
import ua.edu.chnu.labo3.ui.root.AppScaffold

fun main() {
    // Desktop/JVM entry point: start Koin before the UI is created.
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "labo3 Cross Platform",
            // Initial window size (default is 800 x 600), centered on the screen.
            state = rememberWindowState(
                size = DpSize(1000.dp, 800.dp),
                position = WindowPosition(Alignment.Center),
            ),
        ) {
            AppScaffold()
        }
    }
}
