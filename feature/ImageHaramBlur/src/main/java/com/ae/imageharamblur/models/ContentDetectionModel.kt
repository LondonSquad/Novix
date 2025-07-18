package com.ae.imageharamblur.models

import android.content.Context
import android.graphics.Bitmap
import com.ae.imageharamblur.ImageModerationProcessor
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

internal class ContentDetectionModel {

    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor
    private val inputImageWidth: Int
    private val inputImageHeight: Int
    private val inputDataType: DataType
    private val outputSize: Int

    constructor(context: Context) {
        val modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)

        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputImageHeight = inputShape[1]
        this.inputImageWidth = inputShape[2]
        this.inputDataType = inputTensor.dataType()

        val outputTensor = interpreter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        this.outputSize = outputShape[outputShape.size - 1]

        this.imageProcessor = createImageProcessor()

    }

    constructor(modelFile: File) {
        val modelBuffer = loadModelFile(modelFile)

        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputImageHeight = inputShape[1]
        this.inputImageWidth = inputShape[2]
        this.inputDataType = inputTensor.dataType()

        val outputTensor = interpreter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        this.outputSize = outputShape[outputShape.size - 1]

        this.imageProcessor = createImageProcessor()

    }

    private fun loadModelFile(file: File): MappedByteBuffer {
        val fileInputStream = FileInputStream(file)
        val fileChannel = fileInputStream.channel
        val startOffset = 0L
        val declaredLength = fileChannel.size()
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun createInterpreter(modelBuffer: MappedByteBuffer): Interpreter {
        val options = Interpreter.Options().apply {
            numThreads = 4
            useNNAPI = false
        }
        return Interpreter(modelBuffer, options)
    }

    private fun createImageProcessor(): ImageProcessor {

        val builder = ImageProcessor.Builder()
            .add(ResizeOp(inputImageHeight, inputImageWidth, ResizeOp.ResizeMethod.BILINEAR))

        when (inputDataType) {
            DataType.UINT8 -> {
                // Don't add normalization for UINT8
            }
            DataType.FLOAT32 -> {
                builder.add(NormalizeOp(0f, 255f))
            }
            else -> {
                builder.add(NormalizeOp(0f, 255f))
            }
        }

        return builder.build()
    }

    fun detectContent(bitmap: Bitmap): ContentResult {

        return try {
            val tensorImage = TensorImage(inputDataType)
            tensorImage.load(bitmap)

            val processedImage = imageProcessor.process(tensorImage)

            val outputTensor = interpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val outputDataType = outputTensor.dataType()
            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

            outputBuffer.buffer.rewind()

            val probabilities = when (outputDataType) {
                DataType.FLOAT32 -> {
                    val floatArray = FloatArray(outputSize)
                    outputBuffer.buffer.asFloatBuffer().get(floatArray)
                    floatArray
                }
                DataType.UINT8 -> {
                    val byteArray = ByteArray(outputSize)
                    outputBuffer.buffer.get(byteArray)
                    val floatArray = byteArray.map { (it.toInt() and 0xFF) / 255f }.toFloatArray()
                    floatArray
                }
                else -> {
                    FloatArray(outputSize)
                }
            }

            val isInappropriate: Boolean
            val score: Float
            val categoryScores = mutableMapOf<Category, Float>()

            if (outputSize == 2) {
                val safeProb = probabilities[0]
                val unsafeProb = probabilities[1]

                score = unsafeProb
                isInappropriate = unsafeProb > ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD

                categoryScores[Category.NEUTRAL] = safeProb
                categoryScores[Category.PORN] = unsafeProb * 0.5f
                categoryScores[Category.SEXY] = unsafeProb * 0.3f
                categoryScores[Category.HENTAI] = unsafeProb * 0.2f
                categoryScores[Category.DRAWING] = 0f

            } else if (outputSize >= 5) {
                Category.entries.forEach { category ->
                    if (category.index < probabilities.size) {
                        val prob = probabilities[category.index]
                        categoryScores[category] = prob
                    }
                }

                val pornScore = categoryScores[Category.PORN] ?: 0f
                val sexyScore = categoryScores[Category.SEXY] ?: 0f
                val hentaiScore = categoryScores[Category.HENTAI] ?: 0f

                score = pornScore + sexyScore + hentaiScore
                isInappropriate = score > ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD

            } else {
                score = 0f
                isInappropriate = false
            }

            ContentResult(
                isInappropriate = isInappropriate,
                score = score,
                categoryScores = categoryScores
            )
        } catch (e: Exception) {
            e.printStackTrace()
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

    enum class Category(val index: Int) {
        DRAWING(0),
        HENTAI(1),
        NEUTRAL(2),
        PORN(3),
        SEXY(4)
    }

    companion object {
        private const val MODEL_FILE = "nsfw_model.tflite"
    }
}

internal data class ContentResult(
    val isInappropriate: Boolean,
    val score: Float,
    val categoryScores: Map<ContentDetectionModel.Category, Float>
) {
    override fun toString(): String {
        return "ContentResult(isInappropriate=$isInappropriate, score=$score, categories=$categoryScores)"
    }
}