package com.london.imageharamblur.models

import android.content.Context
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean
import androidx.core.content.edit

class ModelDownloadManager(context: Context) {

    private val appContext = context.applicationContext
    private val modelDir = File(appContext.filesDir, "models")
    private val prefs = appContext.getSharedPreferences("model_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val GENDER_MODEL_NAME = "gender_class_model"
        private const val NSFW_MODEL_NAME = "nsfw-detection-model"
        private const val PREF_GENDER_MODEL_VERSION = "gender_model_version"
        private const val PREF_NSFW_MODEL_VERSION = "nsfw_model_version"
        private const val PREF_FIREBASE_MODELS_READY = "firebase_models_ready"

        @Volatile
        private var downloadJob: Job? = null
        private val isDownloading = AtomicBoolean(false)

        private val downloadScope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, _ -> }
        )
    }

    data class ModelFiles(
        val genderModelFile: File,
        val nsfwModelFile: File,
        val isFromFirebase: Boolean = false
    )

    init {
        if (!modelDir.exists()) {
            modelDir.mkdirs()
        }
    }

    suspend fun downloadModelsIfNeeded(): ModelFiles = withContext(Dispatchers.IO) {
        try {
            val firebaseModelsReady = prefs.getBoolean(PREF_FIREBASE_MODELS_READY, false)
            if (firebaseModelsReady) {
                val localFirebaseModels = getLocalFirebaseModels()
                if (localFirebaseModels != null) {
                    return@withContext localFirebaseModels
                } else {
                    prefs.edit { putBoolean(PREF_FIREBASE_MODELS_READY, false) }
                }
            }

            startBackgroundDownloadIfNeeded()
            return@withContext getLocalModelFiles()

        } catch (e: Exception) {
            if (e !is CancellationException) {
                return@withContext getLocalModelFiles()
            }
            throw e
        }
    }

    private fun startBackgroundDownloadIfNeeded() {
        if (isDownloading.compareAndSet(false, true)) {
            downloadJob = downloadScope.launch {
                try {
                    downloadFirebaseModels()
                } finally {
                    isDownloading.set(false)
                }
            }
        }
    }

    private suspend fun downloadFirebaseModels() {
        try {
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi()
                .build()

            val genderModel = FirebaseModelDownloader.getInstance()
                .getModel(GENDER_MODEL_NAME, DownloadType.LATEST_MODEL, conditions)
                .await()

            val nsfwModel = FirebaseModelDownloader.getInstance()
                .getModel(NSFW_MODEL_NAME, DownloadType.LATEST_MODEL, conditions)
                .await()

            if (genderModel?.file != null && nsfwModel?.file != null) {
                if (genderModel.file!!.exists() && genderModel.file!!.length() > 0 &&
                    nsfwModel.file!!.exists() && nsfwModel.file!!.length() > 0) {

                    saveModelVersions(genderModel, nsfwModel)
                    prefs.edit { putBoolean(PREF_FIREBASE_MODELS_READY, true) }
                }
            }
        } catch (e: Exception) {
            // Silent fail
        }
    }

    private suspend fun getLocalFirebaseModels(): ModelFiles? = withContext(Dispatchers.IO) {
        try {
            val conditions = CustomModelDownloadConditions.Builder().build()

            val genderModel = FirebaseModelDownloader.getInstance()
                .getModel(GENDER_MODEL_NAME, DownloadType.LOCAL_MODEL, conditions)
                .await()

            val nsfwModel = FirebaseModelDownloader.getInstance()
                .getModel(NSFW_MODEL_NAME, DownloadType.LOCAL_MODEL, conditions)
                .await()

            if (genderModel?.file != null && nsfwModel?.file != null &&
                genderModel.file!!.exists() && nsfwModel.file!!.exists() &&
                genderModel.file!!.length() > 0 && nsfwModel.file!!.length() > 0) {

                return@withContext ModelFiles(
                    genderModelFile = genderModel.file!!,
                    nsfwModelFile = nsfwModel.file!!,
                    isFromFirebase = true
                )
            }
        } catch (e: Exception) {
            // Silent fail
        }

        return@withContext null
    }

    private fun saveModelVersions(genderModel: CustomModel, nsfwModel: CustomModel) {
        prefs.edit().apply {
            putString(PREF_GENDER_MODEL_VERSION, genderModel.modelHash)
            putString(PREF_NSFW_MODEL_VERSION, nsfwModel.modelHash)
            apply()
        }
    }

    private fun getLocalModelFiles(): ModelFiles {
        return ModelFiles(
            genderModelFile = File(""),
            nsfwModelFile = File(""),
            isFromFirebase = false
        )
    }

}