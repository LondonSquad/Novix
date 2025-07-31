package com.london.data.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

interface CrashReporter {
    fun logException(exception: Throwable)
}
class FirebaseCrashReporter @Inject constructor() : CrashReporter {
    override fun logException(exception: Throwable) {
        FirebaseCrashlytics.getInstance().apply {
            setCustomKey("operation", "un_known_error")
            setCustomKey("error_type", "{${exception.javaClass.name}}")
            setCustomKey("timestamp", System.currentTimeMillis())
            recordException(exception)
        }
    }
}