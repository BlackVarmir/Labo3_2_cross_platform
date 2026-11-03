package ua.edu.chnu.labo3

import co.touchlab.kermit.Logger
import java.awt.Toolkit

actual class Platform actual constructor() {
    actual val osName: String
        get() = System.getProperty("os.name") ?: "Desktop"
    actual val osVersion: String
        get() = System.getProperty("os.version") ?: "---"
    actual val deviceModel: String
        get() = "Desktop (JVM ${System.getProperty("java.version")})"
    actual val cpuType: String
        get() = System.getProperty("os.arch") ?: "---"

    actual val screen: ScreenInfo
        get() = ScreenInfo()

    actual fun logSystemInfo() {
        Logger.d(tag = "Platform") { "System info: $deviceInfo" }
    }
}

actual class ScreenInfo actual constructor() {

    private val screenSize = Toolkit.getDefaultToolkit().screenSize

    actual val width: Int
        get() = screenSize.width
    actual val height: Int
        get() = screenSize.height
    actual val density: Int?
        get() = null
}
