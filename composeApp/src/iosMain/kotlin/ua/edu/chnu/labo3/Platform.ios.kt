package ua.edu.chnu.labo3

import co.touchlab.kermit.Logger
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import kotlin.math.round

actual class Platform actual constructor() {
    private val device = UIDevice.currentDevice

    actual val osName: String
        get() = device.systemName
    actual val osVersion: String
        get() = device.systemVersion
    actual val deviceModel: String
        get() = device.model
    actual val cpuType: String
        get() = "arm64"
    actual val screen: ScreenInfo
        get() = ScreenInfo()

    actual fun logSystemInfo() {
        Logger.d(tag = "Platform") { "System info: $deviceInfo" }
    }
}

actual class ScreenInfo actual constructor() {

    private val scale = UIScreen.mainScreen.scale

    @OptIn(ExperimentalForeignApi::class)
    actual val width: Int
        get() = UIScreen.mainScreen.bounds.useContents { (size.width * scale).toInt() }

    @OptIn(ExperimentalForeignApi::class)
    actual val height: Int
        get() = UIScreen.mainScreen.bounds.useContents { (size.height * scale).toInt() }

    actual val density: Int?
        get() = round(scale).toInt()
}
