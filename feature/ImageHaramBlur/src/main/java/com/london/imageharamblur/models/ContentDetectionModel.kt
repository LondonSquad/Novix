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
        FileInputStream(file).use { fileInputStream ->
            val fileChannel = fileInputStream.channel
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size())
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
            val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, outputSize), DataType.FLOAT32)

            interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())

            val probabilities = FloatArray(outputSize)
            outputBuffer.buffer.rewind()
            outputBuffer.buffer.asFloatBuffer().get(probabilities)

            val isInappropriate = if (outputSize == 2) {
                probabilities[1] > DEFAULT_CONTENT_THRESHOLD
            } else {
                val inappropriateScore = (probabilities.getOrNull(3) ?: 0f) + // PORN
                        (probabilities.getOrNull(4) ?: 0f) + // SEXY
                        (probabilities.getOrNull(1) ?: 0f)   // HENTAI
                inappropriateScore > DEFAULT_CONTENT_THRESHOLD
            }

            ContentResult(isInappropriate = isInappropriate)
        } catch (e: Exception) {
            e.printStackTrace()
            ContentResult(isInappropriate = false)
        }
    }

    fun close() {
        interpreter.close()
    }

    companion object {
        private const val MODEL_FILE = "nsfw_model.tflite"
        private const val DEFAULT_CONTENT_THRESHOLD = 0.3f
    }
}

internal data class ContentResult(
    val isInappropriate: Boolean
)