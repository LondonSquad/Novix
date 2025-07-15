package com.london.data.datasource.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

interface CrashReporter {
    fun logException(exception: Throwable)
}

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
