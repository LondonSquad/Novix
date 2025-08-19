package com.london.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.london.domain.repository.CustomMovieListRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class MovieListSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: CustomMovieListRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            repository.refreshMovieListCache()
            Result.success()
        } catch (_: Exception) {
            if (runAttemptCount < MAX_RETRY_ATTEMPTS) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        const val WORK_NAME = "movie_list_sync"
        const val IMMEDIATE_SYNC_WORK_NAME = "movie_list_sync_immediate"
        private const val MAX_RETRY_ATTEMPTS = 3

        fun schedulePeriodicSync(workManager: WorkManager) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<MovieListSyncWorker>(
                repeatInterval = 30,
                repeatIntervalTimeUnit = TimeUnit.MINUTES,
                flexTimeInterval = 10,
                flexTimeIntervalUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag(WORK_NAME)
                .build()

            workManager.enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }
    }
}