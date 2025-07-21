package com.london.imageharamblur.models

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
        return _downloadState.value.nsfwModelReady &&
                _downloadState.value.genderModelReady &&
                _downloadState.value.faceDetectionModelReady
    }

    private fun checkExistingModels() {
        val assetsNsfwExists = tryAssetsModel("nsfw_model.tflite")
        val assetsGenderExists = tryAssetsModel("gender_class_model.tflite")
        val assetsFaceDetectionExists = tryAssetsModel("face_detection_back.tflite")

        if (assetsNsfwExists && assetsGenderExists && assetsFaceDetectionExists) {
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                genderModelReady = true,
                faceDetectionModelReady = true,
                usingLocalAssets = true
            )
            return
        }

        val nsfwModelPath = prefs.getString("nsfw_model_path", null)
        val genderModelPath = prefs.getString("gender_model_path", null)
        val faceDetectionModelPath = prefs.getString("face_detection_model_path", null)

        val nsfwReady = nsfwModelPath?.let { File(it).exists() } == true
        val genderReady = genderModelPath?.let { File(it).exists() } == true
        val faceDetectionReady = faceDetectionModelPath?.let { File(it).exists() } == true

        _downloadState.value = _downloadState.value.copy(
            nsfwModelReady = nsfwReady || assetsNsfwExists,
            genderModelReady = genderReady || assetsGenderExists,
            faceDetectionModelReady = faceDetectionReady || assetsFaceDetectionExists,
            usingLocalAssets = assetsNsfwExists || assetsGenderExists || assetsFaceDetectionExists
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
                Log.d(TAG, "Using cached asset file: $assetFileName (${cacheFile.length()} bytes)")
                return cacheFile
            }

            context.assets.open(assetFileName).use { inputStream ->
                FileOutputStream(cacheFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            Log.d(TAG, "Copied asset to cache: $assetFileName (${cacheFile.length()} bytes)")
            cacheFile
        } catch (e: Exception) {
            Log.e(TAG, "Failed to copy asset $assetFileName", e)
            null
        }
    }

    suspend fun downloadModelsIfNeeded(wifiOnly: Boolean = true): ModelFiles {
        val nsfwAssetFile = copyAssetToFile("nsfw_model.tflite")
        val genderAssetFile = copyAssetToFile("gender_class_model.tflite")
        val faceDetectionAssetFile = copyAssetToFile("face_detection_back.tflite")

        if (nsfwAssetFile != null && genderAssetFile != null && faceDetectionAssetFile != null) {
            Log.d(TAG, "Using all models from assets")
            _downloadState.value = _downloadState.value.copy(
                nsfwModelReady = true,
                genderModelReady = true,
                faceDetectionModelReady = true,
                usingLocalAssets = true
            )
            return ModelFiles(
                nsfwModelFile = nsfwAssetFile,
                genderModelFile = genderAssetFile,
                faceDetectionModelFile = faceDetectionAssetFile
            )
        }

        if (_downloadState.value.nsfwModelReady &&
            _downloadState.value.genderModelReady &&
            _downloadState.value.faceDetectionModelReady) {

            val nsfwPath = nsfwAssetFile ?: prefs.getString("nsfw_model_path", null)?.let { File(it) }
            val genderPath = genderAssetFile ?: prefs.getString("gender_model_path", null)?.let { File(it) }
            val faceDetectionPath = faceDetectionAssetFile ?: prefs.getString("face_detection_model_path", null)?.let { File(it) }

            if (nsfwPath != null && genderPath != null && faceDetectionPath != null) {
                return ModelFiles(
                    nsfwModelFile = nsfwPath,
                    genderModelFile = genderPath,
                    faceDetectionModelFile = faceDetectionPath
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

            // Download NSFW model
            val nsfwModel = if (nsfwAssetFile == null) {
                _downloadState.value = _downloadState.value.copy(
                    currentDownloadingModel = "NSFW Detection Model"
                )
                val model = downloadModel(NSFW_MODEL_NAME, conditions)
                saveModelPath("nsfw_model_path", model.file?.path)
                _downloadState.value = _downloadState.value.copy(
                    nsfwModelReady = true,
                    downloadProgress = 0.33f
                )
                model.file!!
            } else {
                nsfwAssetFile
            }

            // Download Gender model
            val genderModel = if (genderAssetFile == null) {
                _downloadState.value = _downloadState.value.copy(
                    currentDownloadingModel = "Gender Classification Model"
                )
                val model = downloadModel(GENDER_MODEL_NAME, conditions)
                saveModelPath("gender_model_path", model.file?.path)
                _downloadState.value = _downloadState.value.copy(
                    genderModelReady = true,
                    downloadProgress = 0.66f
                )
                model.file!!
            } else {
                genderAssetFile
            }

            // Download Face Detection model
            val faceDetectionModel = if (faceDetectionAssetFile == null) {
                _downloadState.value = _downloadState.value.copy(
                    currentDownloadingModel = "Face Detection Model"
                )
                val model = downloadModel(FACE_DETECTION_MODEL_NAME, conditions)
                saveModelPath("face_detection_model_path", model.file?.path)
                _downloadState.value = _downloadState.value.copy(
                    faceDetectionModelReady = true,
                    downloadProgress = 1.0f,
                    isDownloading = false,
                    currentDownloadingModel = null
                )
                model.file!!
            } else {
                _downloadState.value = _downloadState.value.copy(
                    faceDetectionModelReady = true,
                    downloadProgress = 1.0f,
                    isDownloading = false,
                    currentDownloadingModel = null
                )
                faceDetectionAssetFile
            }

            return ModelFiles(
                nsfwModelFile = nsfwModel,
                genderModelFile = genderModel,
                faceDetectionModelFile = faceDetectionModel
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading models", e)
            _downloadState.value = _downloadState.value.copy(
                isDownloading = false,
                error = e.message,
                currentDownloadingModel = null
            )

            // Fallback to any available asset models
            if (nsfwAssetFile != null && genderAssetFile != null) {
                Log.w(TAG, "Download failed, falling back to asset models (face detection might be unavailable)")

                // Try to use a default face detection model or create a dummy file
                val fallbackFaceDetection = faceDetectionAssetFile ?: File(context.cacheDir, "face_detection_fallback.tflite").apply {
                    if (!exists()) {
                        Log.w(TAG, "Face detection model not available, face detection will not work")
                    }
                }

                return ModelFiles(
                    nsfwModelFile = nsfwAssetFile,
                    genderModelFile = genderAssetFile,
                    faceDetectionModelFile = fallbackFaceDetection
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
        private const val FACE_DETECTION_MODEL_NAME = "face_detection_back"
        private const val PREF_NAME = "model_download_prefs"
    }

    data class ModelDownloadState(
        val isDownloading: Boolean = false,
        val nsfwModelReady: Boolean = false,
        val genderModelReady: Boolean = false,
        val faceDetectionModelReady: Boolean = false,
        val downloadProgress: Float = 0f,
        val error: String? = null,
        val currentDownloadingModel: String? = null,
        val totalSizeMB: Float = 29.3f,
        val usingLocalAssets: Boolean = false
    )

    data class ModelFiles(
        val nsfwModelFile: File,
        val genderModelFile: File,
        val faceDetectionModelFile: File
    )
}