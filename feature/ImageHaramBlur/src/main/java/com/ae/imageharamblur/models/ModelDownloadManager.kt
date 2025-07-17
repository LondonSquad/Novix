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
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import androidx.core.content.edit

class ModelDownloadManager(private val context: Context) {

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
        val assetsNsfwExists = tryAssetsModel("nsfw_model.tflite")
        val assetsGenderExists = tryAssetsModel("gender_class_model.tflite")

        if (assetsNsfwExists && assetsGenderExists) {
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                genderModelReady = true,
                usingLocalAssets = true
            )
            return
        }

        val nsfwModelPath = prefs.getString("nsfw_model_path", null)
        val genderModelPath = prefs.getString("gender_model_path", null)

        val nsfwReady = nsfwModelPath?.let { File(it).exists() } == true
        val genderReady = genderModelPath?.let { File(it).exists() } == true

        _downloadState.value = _downloadState.value.copy(
            nsfwModelReady = nsfwReady || assetsNsfwExists,
            genderModelReady = genderReady || assetsGenderExists,
            usingLocalAssets = assetsNsfwExists || assetsGenderExists
        )
    }

    private fun tryAssetsModel(fileName: String): Boolean {
        return try {
            context.assets.open(fileName).use { inputStream ->
                inputStream.available() > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun copyAssetToFile(assetFileName: String): File? {
        return try {
            val cacheFile = File(context.cacheDir, assetFileName)
            if (cacheFile.exists() && cacheFile.length() > 0) {
                return cacheFile
            }

            context.assets.open(assetFileName).use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            cacheFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy asset $assetFileName", e)
            null
        }
    }

    suspend fun downloadModelsIfNeeded(wifiOnly: Boolean = true): ModelFiles {
        val nsfwAssetFile = copyAssetToFile("nsfw_model.tflite")
        val genderAssetFile = copyAssetToFile("gender_class_model.tflite")

        if (nsfwAssetFile != null && genderAssetFile != null) {
            Log.d(TAG, "Using models from assets")
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                genderModelReady = true,
                usingLocalAssets = true
            )
            return ModelFiles(
                nsfwModelFile = nsfwAssetFile,
                genderModelFile = genderAssetFile
            )
        }

        if (_downloadState.value.nsfwModelReady && _downloadState.value.genderModelReady) {
            val nsfwPath = nsfwAssetFile ?: prefs.getString("nsfw_model_path", null)?.let { File(it) }
            val genderPath = genderAssetFile ?: prefs.getString("gender_model_path", null)?.let { File(it) }

            if (nsfwPath != null && genderPath != null) {
                return ModelFiles(
                    nsfwModelFile = nsfwPath,
                    genderModelFile = genderPath
                )
            }
        }

        _downloadState.value = _downloadState.value.copy(
            isDownloading = true,
            error = null,
            downloadProgress = 0f,
            usingLocalAssets = false
        )

        try {
            val conditionsBuilder = CustomModelDownloadConditions.Builder()
            if (wifiOnly) {
                conditionsBuilder.requireWifi()
            }
            val conditions = conditionsBuilder.build()

            val nsfwModel = if (nsfwAssetFile == null) {
                _downloadState.value = _downloadState.value.copy(
                    currentDownloadingModel = "NSFW Detection Model"
                )
                val model = downloadModel(NSFW_MODEL_NAME, conditions)
                saveModelPath("nsfw_model_path", model.file?.path)
                _downloadState.value = _downloadState.value.copy(
                    nsfwModelReady = true,
                    downloadProgress = 0.5f
                )
                model.file!!
            } else {
                nsfwAssetFile
            }
            val genderModel = if (genderAssetFile == null) {
                _downloadState.value = _downloadState.value.copy(
                    currentDownloadingModel = "Gender Classification Model"
                )
                val model = downloadModel(GENDER_MODEL_NAME, conditions)
                saveModelPath("gender_model_path", model.file?.path)
                _downloadState.value = _downloadState.value.copy(
                    genderModelReady = true,
                    downloadProgress = 1.0f,
                    isDownloading = false,
                    currentDownloadingModel = null
                )
                model.file!!
            } else {
                genderAssetFile
            }

            return ModelFiles(
                nsfwModelFile = nsfwModel,
                genderModelFile = genderModel
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading models", e)
            _downloadState.value = _downloadState.value.copy(
                isDownloading = false,
                error = e.message,
                currentDownloadingModel = null
            )

            if (nsfwAssetFile != null && genderAssetFile != null) {
                Log.w(TAG, "Download failed, falling back to asset models")
                return ModelFiles(
                    nsfwModelFile = nsfwAssetFile,
                    genderModelFile = genderAssetFile
                )
            }

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
        val totalSizeMB: Float = 29.0f,
        val usingLocalAssets: Boolean = false
    )

    data class ModelFiles(
        val nsfwModelFile: File,
        val genderModelFile: File
    )
}