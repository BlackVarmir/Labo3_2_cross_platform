package ua.edu.chnu.labo3

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import ua.edu.chnu.labo3.di.initKoin

/**
 * Android entry point.
 *
 * Koin is started here, as early as possible in the app lifecycle, and is given
 * the Android [Application] context plus a logger.
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger(Level.INFO)
            androidContext(this@MainApplication)
        }
    }
}
