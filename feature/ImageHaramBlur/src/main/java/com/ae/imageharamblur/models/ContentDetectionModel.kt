package com.ae.imageharamblur.models

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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

    constructor(context: Context) {
        val modelBuffer = FileUtil.loadMappedFile(context, "nsfw_model.tflite")
        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputImageHeight = inputShape[1]
        this.inputImageWidth = inputShape[2]
        this.inputDataType = inputTensor.dataType()

        Log.d("ContentModel", "Input shape: ${inputShape.contentToString()}, dataType: $inputDataType")

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

        Log.d("ContentModel", "Input shape: ${inputShape.contentToString()}, dataType: $inputDataType")

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

        // Add normalization based on data type
        when (inputDataType) {
            DataType.UINT8 -> {
                // For quantized models (uint8), typically no normalization or scale to [0,1]
                builder.add(NormalizeOp(0f, 1f))
            }
            DataType.FLOAT32 -> {
                // For float models
                builder.add(NormalizeOp(0f, 255f))
            }
            else -> {
                // Default normalization
                builder.add(NormalizeOp(0f, 255f))
            }
        }

        return builder.build()
    }

    fun detectContent(bitmap: Bitmap): ContentResult {
        return try {
            // Create TensorImage with proper type
            val tensorImage = TensorImage(inputDataType)
            tensorImage.load(bitmap)

            // Process the image
            val processedImage = imageProcessor.process(tensorImage)

            // Get output tensor info
            val outputTensor = interpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val outputDataType = outputTensor.dataType()

            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            // Run inference
            interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

            // Convert output to float array based on data type
            val probabilities = when (outputDataType) {
                DataType.FLOAT32 -> outputBuffer.floatArray
                DataType.UINT8 -> {
                    // Convert uint8 to float probabilities
                    val byteArray = ByteArray(outputBuffer.buffer.remaining())
                    outputBuffer.buffer.get(byteArray)
                    byteArray.map { (it.toInt() and 0xFF) / 255f }.toFloatArray()
                }
                else -> outputBuffer.floatArray
            }

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
            Log.e("ContentDetectionModel", "Error detecting content", e)
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
}

internal data class ContentResult(
    val isInappropriate: Boolean,
    val score: Float,
    val categoryScores: Map<ContentDetectionModel.Category, Float>
)