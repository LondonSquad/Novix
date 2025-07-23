package com.london.app

import android.app.Application
import com.london.app.di.AppModule
import com.london.data.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.ksp.generated.module
import timber.log.Timber

class NovixApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        timberConfig()
        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@NovixApplication)
            modules(AppModule().module)
        }
    }

    private fun timberConfig() {
        if (BuildConfig.DEBUG)
            Timber.Forest.plant(object : Timber.DebugTree() {
                /**
                 * Override [log] to modify the tag and add a "global tag" prefix to it. You can rename the String "global_tag_" as you see fit.
                 */
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, "DEBUGGING", "$tag -> $message", t)
                }

                /**
                 * Override [createStackElementTag] to include a add a "method name" to the tag.
                 */
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "%s:%s$%s()",
                        element.fileName,
                        element.lineNumber,
                        element.methodName,
                    )
                }
            })
    }
}