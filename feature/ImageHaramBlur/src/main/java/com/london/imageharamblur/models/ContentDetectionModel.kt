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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class ContentDetectionModel {

    // Thread-local interpreter instances
    private val interpreterThreadLocal = ThreadLocal<Interpreter?>()
    private val modelBuffer: MappedByteBuffer?
    private val imageProcessor: ImageProcessor
    private val inputImageWidth: Int
    private val inputImageHeight: Int
    private val inputDataType: DataType
    private val outputSize: Int
    private val outputDataType: DataType

    // Mutex for thread-safe access
    private val interpreterLock = Mutex()

    constructor(context: Context) {
        try {
            modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)

            // Initialize one interpreter to get model info, then close it
            val tempInterpreter = createInterpreter(modelBuffer)

            // Validate model has required tensors
            if (tempInterpreter.inputTensorCount == 0) {
                tempInterpreter.close()
                throw Exception("Model has no input tensors")
            }
            if (tempInterpreter.outputTensorCount == 0) {
                tempInterpreter.close()
                throw Exception("Model has no output tensors")
            }

            val inputTensor = tempInterpreter.getInputTensor(0)
            val inputShape = inputTensor.shape()
            this.inputImageHeight = inputShape[1]
            this.inputImageWidth = inputShape[2]
            this.inputDataType = inputTensor.dataType()

            val outputTensor = tempInterpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            this.outputSize = outputShape[outputShape.size - 1]
            this.outputDataType = outputTensor.dataType()

            tempInterpreter.close()

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

            modelBuffer = loadModelFile(modelFile)

            // Initialize one interpreter to get model info, then close it
            val tempInterpreter = createInterpreter(modelBuffer)

            // Validate model has required tensors
            if (tempInterpreter.inputTensorCount == 0) {
                tempInterpreter.close()
                throw Exception("Model has no input tensors")
            }
            if (tempInterpreter.outputTensorCount == 0) {
                tempInterpreter.close()
                throw Exception("Model has no output tensors")
            }

            val inputTensor = tempInterpreter.getInputTensor(0)
            val inputShape = inputTensor.shape()
            this.inputImageHeight = inputShape[1]
            this.inputImageWidth = inputShape[2]
            this.inputDataType = inputTensor.dataType()

            val outputTensor = tempInterpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            this.outputSize = outputShape[outputShape.size - 1]
            this.outputDataType = outputTensor.dataType()

            tempInterpreter.close()

            this.imageProcessor = createImageProcessor()
        } catch (e: Exception) {
            throw Exception("Failed to initialize model: ${e.message}")
        }
    }

    private fun getOrCreateInterpreter(): Interpreter? {
        var interpreter = interpreterThreadLocal.get()
        if (interpreter == null && modelBuffer != null) {
            interpreter = createInterpreter(modelBuffer)
            interpreterThreadLocal.set(interpreter)
        }
        return interpreter
    }

    private fun loadModelFile(file: File): MappedByteBuffer {
        return FileInputStream(file).use { fileInputStream ->
            val fileChannel = fileInputStream.channel
            fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileChannel.size())
        }
    }

    private fun createInterpreter(modelBuffer: MappedByteBuffer): Interpreter {
        val options = Interpreter.Options().apply {
            setNumThreads(1)
            setUseNNAPI(false)
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

    suspend fun detectContent(bitmap: Bitmap): ContentResult = withContext(Dispatchers.IO) {
        interpreterLock.withLock {
            try {
                val interpreter = getOrCreateInterpreter() ?: return@withContext ContentResult(isInappropriate = false)

                // Validate interpreter state
                if (interpreter.outputTensorCount == 0) {
                    return@withContext ContentResult(isInappropriate = false)
                }

                val tensorImage = TensorImage(inputDataType)
                tensorImage.load(bitmap)

                val processedImage = imageProcessor.process(tensorImage)

                // Use pre-computed output info instead of calling getOutputTensor again
                val outputBuffer = TensorBuffer.createFixedSize(
                    intArrayOf(1, outputSize),
                    outputDataType
                )

                // Thread-safe execution
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
                android.util.Log.e("ContentDetectionModel", "Error detecting content", e)
                ContentResult(isInappropriate = false)
            }
        }
    }

    fun close() {
        try {0
            interpreterThreadLocal.get()?.close()
            interpreterThreadLocal.remove()
        } catch (e: Exception) {}
    }

    companion object {
        private const val MODEL_FILE = "nsfw_model.tflite"
        private const val DEFAULT_CONTENT_THRESHOLD = 0.3f
    }
}

internal data class ContentResult(
    val isInappropriate: Boolean
)