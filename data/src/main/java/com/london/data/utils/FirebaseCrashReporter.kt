package com.london.data.datasource.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.core.annotation.Single

interface CrashReporter {
    fun logException(exception: Throwable)
}
@Single
class FirebaseCrashReporter : CrashReporter {
    override fun logException(exception: Throwable) {
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("operation", "un_known_error")
            setCustomKey("error_type", "{${exception.javaClass.name}}")
            setCustomKey("timestamp", System.currentTimeMillis())
            recordException(exception)
        }
    }
}