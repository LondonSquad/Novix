package com.ae.imageharamblur.models

import android.content.Context
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ModelDownloadManager(private val context: Context) {

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val modelDownloader = FirebaseModelDownloader.getInstance()

    private val _downloadState = MutableStateFlow(ModelDownloadState())
    val downloadState: StateFlow<ModelDownloadState> = _downloadState

    data class ModelDownloadState(
        val isDownloading: Boolean = false,
        val nsfwModelReady: Boolean = false,
        val genderModelReady: Boolean = false,
        val downloadProgress: Float = 0f,
        val error: String? = null
    )

    data class ModelFiles(
        val nsfwModelFile: File,
        val genderModelFile: File
    )

    init {
        // Check if models are already downloaded
        checkExistingModels()
    }

    private fun checkExistingModels() {
        val nsfwModelPath = prefs.getString("nsfw_model_path", null)
        val genderModelPath = prefs.getString("gender_model_path", null)

        val nsfwReady = nsfwModelPath?.let { File(it).exists() } ?: false
        val genderReady = genderModelPath?.let { File(it).exists() } ?: false

        _downloadState.value = _downloadState.value.copy(
            nsfwModelReady = nsfwReady,
            genderModelReady = genderReady
        )
    }

    suspend fun downloadModelsIfNeeded(): ModelFiles {
        if (_downloadState.value.nsfwModelReady && _downloadState.value.genderModelReady) {
            return ModelFiles(
                nsfwModelFile = File(prefs.getString("nsfw_model_path", "")!!),
                genderModelFile = File(prefs.getString("gender_model_path", "")!!)
            )
        }

        _downloadState.value = _downloadState.value.copy(isDownloading = true, error = null)

        try {
            val conditions = CustomModelDownloadConditions.Builder()
                .requireWifi() // Download only on WiFi
                .build()

            // Download NSFW model
            val nsfwModel = downloadModel(NSFW_MODEL_NAME, conditions)
            saveModelPath("nsfw_model_path", nsfwModel.file?.path)
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                downloadProgress = 0.5f
            )

            // Download Gender model
            val genderModel = downloadModel(GENDER_MODEL_NAME, conditions)
            saveModelPath("gender_model_path", genderModel.file?.path)
            _downloadState.value = _downloadState.value.copy(
                genderModelReady = true,
                downloadProgress = 1.0f,
                isDownloading = false
            )

            return ModelFiles(
                nsfwModelFile = nsfwModel.file!!,
                genderModelFile = genderModel.file!!
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading models", e)
            _downloadState.value = _downloadState.value.copy(
                isDownloading = false,
                error = e.message
            )
            throw e
        }
    }

    private suspend fun downloadModel(
        modelName: String,
        conditions: CustomModelDownloadConditions
    ): CustomModel = suspendCancellableCoroutine { cont ->
        modelDownloader
            .getModel(modelName, DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model ->
                Log.d(TAG, "Model downloaded successfully: $modelName")
                cont.resume(model)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to download model: $modelName", exception)
                cont.resumeWithException(exception)
            }
    }

    private fun saveModelPath(key: String, path: String?) {
        path?.let {
            prefs.edit().putString(key, it).apply()
        }
    }

    fun deleteDownloadedModels() {
        listOf("nsfw_model_path", "gender_model_path").forEach { key ->
            prefs.getString(key, null)?.let { path ->
                File(path).delete()
            }
            prefs.edit().remove(key).apply()
        }
        checkExistingModels()
    }

    companion object {
        private const val TAG = "ModelDownloadManager"
        private const val NSFW_MODEL_NAME = "nsfw_detection_model"
        private const val GENDER_MODEL_NAME = "gender_classification_model"
        private const val PREF_NAME = "model_download_prefs"
        private const val KEY_NSFW_VERSION = "nsfw_model_version"
        private const val KEY_GENDER_VERSION = "gender_model_version"
    }
}