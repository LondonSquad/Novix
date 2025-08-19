package com.london.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.london.data.BuildConfig
import com.london.data.worker.MovieListSyncWorker
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class NovixApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        timberConfig()
        WorkManager.initialize(this, workManagerConfiguration)
        MovieListSyncWorker.schedulePeriodicSync(WorkManager.getInstance(applicationContext))
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
