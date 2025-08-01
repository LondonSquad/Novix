package com.london.imageharamblur.models

import android.content.Context
import android.graphics.Bitmap
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
    private val outputDataType: DataType

    constructor(context: Context) {
        try {
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
            this.outputDataType = outputTensor.dataType()

            this.imageProcessor = createImageProcessor()
        } catch (e: Exception) {
            throw Exception("Failed to initialize model: ${e.message}")
        }
    }

    constructor(modelFile: File) {
        try {
            if (!modelFile.exists() || modelFile.length() == 0L) {
                throw Exception("Invalid model file")
            }

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
            this.outputDataType = outputTensor.dataType()

            this.imageProcessor = createImageProcessor()
        } catch (e: Exception) {
            throw Exception("Failed to initialize model: ${e.message}")
        }
    }

    private fun loadModelFile(file: File): MappedByteBuffer {
        return FileInputStream(file).use { fileInputStream ->
            val fileChannel = fileInputStream.channel
            fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size())
        }
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

        if (inputDataType == DataType.FLOAT32) {
            builder.add(NormalizeOp(0f, 255f))
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
            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

            val probabilities = when (outputDataType) {
                DataType.FLOAT32 -> {
                    val floatArray = FloatArray(outputSize)
                    outputBuffer.buffer.rewind()
                    outputBuffer.buffer.asFloatBuffer().get(floatArray)
                    floatArray
                }
                DataType.UINT8 -> {
                    val byteArray = ByteArray(outputSize)
                    outputBuffer.buffer.rewind()
                    outputBuffer.buffer.get(byteArray)
                    byteArray.map { (it.toInt() and 0xFF) / 255f }.toFloatArray()
                }
                DataType.INT8 -> {
                    val byteArray = ByteArray(outputSize)
                    outputBuffer.buffer.rewind()
                    outputBuffer.buffer.get(byteArray)
                    byteArray.map { (it.toFloat() + 128f) / 255f }.toFloatArray()
                }
                else -> FloatArray(outputSize)
            }

            val isInappropriate = if (outputSize == 2) {
                probabilities[1] > DEFAULT_CONTENT_THRESHOLD
            } else {
                val pornScore = probabilities.getOrNull(3) ?: 0f
                val sexyScore = probabilities.getOrNull(4) ?: 0f
                val hentaiScore = probabilities.getOrNull(1) ?: 0f
                val inappropriateScore = pornScore + sexyScore + hentaiScore
                inappropriateScore > DEFAULT_CONTENT_THRESHOLD
            }

            ContentResult(isInappropriate = isInappropriate)
        } catch (e: Exception) {
            ContentResult(isInappropriate = false)
        }
    }

    fun close() {
        try {
            interpreter.close()
        } catch (e: Exception) {
            // Ignore close errors
        }
    }

    companion object {
        private const val MODEL_FILE = "nsfw_model.tflite"
        private const val DEFAULT_CONTENT_THRESHOLD = 0.3f
    }
}

internal data class ContentResult(
    val isInappropriate: Boolean
)