package com.ae.imageharamblur.models

import android.content.Context
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import androidx.core.content.edit

class ModelDownloadManager(context: Context) {


    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val modelDownloader = FirebaseModelDownloader.getInstance()

    private val _downloadState = MutableStateFlow(ModelDownloadState())
    val downloadState: StateFlow<ModelDownloadState> = _downloadState.asStateFlow()

    init {
        checkExistingModels()
    }

    fun areModelsReady(): Boolean {
        return _downloadState.value.nsfwModelReady && _downloadState.value.genderModelReady
    }

    private fun checkExistingModels() {
        val nsfwModelPath = prefs.getString("nsfw_model_path", null)
        val genderModelPath = prefs.getString("gender_model_path", null)

        val nsfwReady = nsfwModelPath?.let { File(it).exists() } == true
        val genderReady = genderModelPath?.let { File(it).exists() } == true

        _downloadState.value = _downloadState.value.copy(
            nsfwModelReady = nsfwReady,
            genderModelReady = genderReady
        )
    }

    suspend fun downloadModelsIfNeeded(wifiOnly: Boolean = true): ModelFiles {
        if (_downloadState.value.nsfwModelReady && _downloadState.value.genderModelReady) {
            return ModelFiles(
                nsfwModelFile = File(prefs.getString("nsfw_model_path", "")!!),
                genderModelFile = File(prefs.getString("gender_model_path", "")!!)
            )
        }

        _downloadState.value = _downloadState.value.copy(
            isDownloading = true,
            error = null,
            downloadProgress = 0f
        )

        try {
            val conditionsBuilder = CustomModelDownloadConditions.Builder()
            if (wifiOnly) {
                conditionsBuilder.requireWifi()
            }
            val conditions = conditionsBuilder.build()

            // Download NSFW model
            _downloadState.value = _downloadState.value.copy(
                currentDownloadingModel = "NSFW Detection Model"
            )
            val nsfwModel = downloadModel(NSFW_MODEL_NAME, conditions)
            saveModelPath("nsfw_model_path", nsfwModel.file?.path)
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                downloadProgress = 0.5f
            )

            // Download Gender model
            _downloadState.value = _downloadState.value.copy(
                currentDownloadingModel = "Gender Classification Model"
            )
            val genderModel = downloadModel(GENDER_MODEL_NAME, conditions)
            saveModelPath("gender_model_path", genderModel.file?.path)
            _downloadState.value = _downloadState.value.copy(
                genderModelReady = true,
                downloadProgress = 1.0f,
                isDownloading = false,
                currentDownloadingModel = null
            )

            return ModelFiles(
                nsfwModelFile = nsfwModel.file!!,
                genderModelFile = genderModel.file!!
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading models", e)
            _downloadState.value = _downloadState.value.copy(
                isDownloading = false,
                error = e.message,
                currentDownloadingModel = null
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
            prefs.edit { putString(key, it) }
        }
    }


    companion object {
        private const val TAG = "ModelDownloadManager"
        private const val NSFW_MODEL_NAME = "nsfw_model"
        private const val GENDER_MODEL_NAME = "gender_class_model"
        private const val PREF_NAME = "model_download_prefs"
    }

    data class ModelDownloadState(
        val isDownloading: Boolean = false,
        val nsfwModelReady: Boolean = false,
        val genderModelReady: Boolean = false,
        val downloadProgress: Float = 0f,
        val error: String? = null,
        val currentDownloadingModel: String? = null,
        val totalSizeMB: Float = 29.0f
    )

    data class ModelFiles(
        val nsfwModelFile: File,
        val genderModelFile: File
    )
}