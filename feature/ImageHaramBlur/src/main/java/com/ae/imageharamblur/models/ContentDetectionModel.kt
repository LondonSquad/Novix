package com.ae.imageharamblur.models

import android.content.Context
import android.graphics.Bitmap
import com.ae.imageharamblur.ImageModerationProcessor
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

internal class ContentDetectionModel(context: Context) {
    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor
    private val inputImageWidth: Int
    private val inputImageHeight: Int

    enum class Category(val index: Int) {
        DRAWING(0),
        HENTAI(1),
        NEUTRAL(2),
        PORN(3),
        SEXY(4)
    }

    init {
        val modelBuffer = FileUtil.loadMappedFile(context, "nsfw_model.tflite")
        val options = Interpreter.Options().apply {
            numThreads = 4
            useNNAPI = false
        }

        interpreter = Interpreter(modelBuffer, options)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()

        inputImageHeight = inputShape[1]
        inputImageWidth = inputShape[2]

        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputImageHeight, inputImageWidth, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f))
            .build()
    }

    fun detectContent(bitmap: Bitmap): ContentResult {
        return try {
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            val outputShape = interpreter.getOutputTensor(0).shape()
            val outputDataType = interpreter.getOutputTensor(0).dataType()
            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            interpreter.run(tensorImage.buffer, outputBuffer.buffer.rewind())

            val probabilities = outputBuffer.floatArray
            val results = mutableMapOf<Category, Float>()

            Category.entries.forEach { category ->
                if (category.index < probabilities.size) {
                    results[category] = probabilities[category.index]
                }
            }

            val inappropriateScore = (results[Category.PORN] ?: 0f) +
                    (results[Category.SEXY] ?: 0f) +
                    (results[Category.HENTAI] ?: 0f)

            ContentResult(
                isInappropriate = inappropriateScore > ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD,
                score = inappropriateScore,
                categoryScores = results
            )
        } catch (e: Exception) {
            ContentResult(
                isInappropriate = false,
                score = 0f,
                categoryScores = emptyMap()
            )
        }
    }

    fun close() {
        interpreter.close()
    }
}

internal data class ContentResult(
    val isInappropriate: Boolean,
    val score: Float,
    val categoryScores: Map<ContentDetectionModel.Category, Float>
)