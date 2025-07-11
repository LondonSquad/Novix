package com.ae.imageharamblur.models


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.createBitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import kotlin.math.exp

internal class GenderDetectionModel(context: Context) {
    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor

    companion object {
        private const val MODEL_FILE = "gender_class_model.tflite"
        private const val INPUT_SIZE = 224
        private const val IMAGE_MEAN = 127.5f
        private const val IMAGE_STD = 127.5f
        private const val FEMALE_INDEX = 0
        private const val MALE_INDEX = 1
    }

    init {
        val modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)
        val options = Interpreter.Options().apply {
            setNumThreads(4)
        }
        interpreter = Interpreter(modelBuffer, options)

        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(IMAGE_MEAN, IMAGE_STD))
            .build()
    }

    fun detectGender(faceBitmap: Bitmap): GenderResult {
        val rgbBitmap = ensureRgbBitmap(faceBitmap)
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(rgbBitmap))
        val output = Array(1) { FloatArray(2) }

        interpreter.run(tensorImage.buffer, output)

        val femaleProbability = output[0][FEMALE_INDEX]
        val maleProbability = output[0][MALE_INDEX]

        val expFemale = exp(femaleProbability.toDouble())
        val expMale = exp(maleProbability.toDouble())
        val sumExp = expFemale + expMale

        val normalizedFemaleProbability = (expFemale / sumExp).toFloat()
        val normalizedMaleProbability = (expMale / sumExp).toFloat()

        val isFemale = normalizedFemaleProbability > normalizedMaleProbability
        val confidence = if (isFemale) normalizedFemaleProbability else normalizedMaleProbability

        return GenderResult(
            isFemale = isFemale,
            confidence = confidence
        )
    }

    private fun ensureRgbBitmap(bitmap: Bitmap): Bitmap {
        return if (bitmap.config == Bitmap.Config.ARGB_8888) {
            val rgbBitmap = createBitmap(bitmap.width, bitmap.height)
            val canvas = Canvas(rgbBitmap)
            canvas.drawBitmap(bitmap, 0f, 0f, null)
            rgbBitmap
        } else {
            bitmap
        }
    }

    fun close() {
        interpreter.close()
    }
}

internal data class GenderResult(
    val isFemale: Boolean,
    val confidence: Float
)