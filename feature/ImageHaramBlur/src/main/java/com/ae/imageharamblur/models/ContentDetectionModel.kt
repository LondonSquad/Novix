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

    companion object {
        private const val TAG = "ContentDetection"
        private const val MODEL_FILE = "nsfw_model.tflite"
    }

    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor
    private val inputImageWidth: Int
    private val inputImageHeight: Int
    private val inputDataType: DataType
    private val outputSize: Int

    constructor(context: Context) {
        Log.d(TAG, "Loading ContentDetectionModel from assets")
        val modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)
        Log.d(TAG, "Model loaded from assets, size: ${modelBuffer.capacity()} bytes")

        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputImageHeight = inputShape[1]
        this.inputImageWidth = inputShape[2]
        this.inputDataType = inputTensor.dataType()

        // Get output size
        val outputTensor = interpreter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        this.outputSize = outputShape[outputShape.size - 1]  // Get last dimension

        Log.d(TAG, "Input shape: ${inputShape.contentToString()}")
        Log.d(TAG, "Input size: ${inputImageWidth}x${inputImageHeight}")
        Log.d(TAG, "Input data type: $inputDataType")
        Log.d(TAG, "Output size: $outputSize")

        this.imageProcessor = createImageProcessor()

        logModelDetails()
    }

    constructor(modelFile: File) {
        Log.d(TAG, "Loading ContentDetectionModel from file: ${modelFile.absolutePath}")
        val modelBuffer = loadModelFile(modelFile)
        Log.d(TAG, "Model loaded from file, size: ${modelBuffer.capacity()} bytes")

        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputImageHeight = inputShape[1]
        this.inputImageWidth = inputShape[2]
        this.inputDataType = inputTensor.dataType()

        // Get output size
        val outputTensor = interpreter.getOutputTensor(0)
        val outputShape = outputTensor.shape()
        this.outputSize = outputShape[outputShape.size - 1]  // Get last dimension

        Log.d(TAG, "Input shape: ${inputShape.contentToString()}")
        Log.d(TAG, "Input size: ${inputImageWidth}x${inputImageHeight}")
        Log.d(TAG, "Input data type: $inputDataType")
        Log.d(TAG, "Output size: $outputSize")

        this.imageProcessor = createImageProcessor()

        logModelDetails()
    }

    private fun logModelDetails() {
        Log.d(TAG, "=== Model Details ===")
        Log.d(TAG, "Input tensor count: ${interpreter.inputTensorCount}")
        Log.d(TAG, "Output tensor count: ${interpreter.outputTensorCount}")

        for (i in 0 until interpreter.inputTensorCount) {
            val tensor = interpreter.getInputTensor(i)
            Log.d(TAG, "Input $i: shape=${tensor.shape().contentToString()}, dataType=${tensor.dataType()}")
        }

        for (i in 0 until interpreter.outputTensorCount) {
            val tensor = interpreter.getOutputTensor(i)
            Log.d(TAG, "Output $i: shape=${tensor.shape().contentToString()}, dataType=${tensor.dataType()}")
        }
        Log.d(TAG, "===================")
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
        Log.d(TAG, "Creating image processor for dataType: $inputDataType")

        val builder = ImageProcessor.Builder()
            .add(ResizeOp(inputImageHeight, inputImageWidth, ResizeOp.ResizeMethod.BILINEAR))

        // Don't normalize for UINT8 input - the model expects 0-255 values
        when (inputDataType) {
            DataType.UINT8 -> {
                Log.d(TAG, "No normalization for UINT8 input")
                // Don't add normalization for UINT8
            }
            DataType.FLOAT32 -> {
                Log.d(TAG, "Adding normalization for FLOAT32: [0, 255]")
                builder.add(NormalizeOp(0f, 255f))
            }
            else -> {
                Log.d(TAG, "Adding default normalization: [0, 255]")
                builder.add(NormalizeOp(0f, 255f))
            }
        }

        return builder.build()
    }

    fun detectContent(bitmap: Bitmap): ContentResult {
        Log.d(TAG, "detectContent called with bitmap: ${bitmap.width}x${bitmap.height}")

        return try {
            val tensorImage = TensorImage(inputDataType)
            tensorImage.load(bitmap)
            Log.d(TAG, "TensorImage loaded")

            val processedImage = imageProcessor.process(tensorImage)
            Log.d(TAG, "Image processed, buffer size: ${processedImage.buffer.remaining()} bytes")

            val outputTensor = interpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val outputDataType = outputTensor.dataType()

            Log.d(TAG, "Output shape: ${outputShape.contentToString()}")
            Log.d(TAG, "Output data type: $outputDataType")

            // Create output buffer
            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            // Run inference
            val startTime = System.currentTimeMillis()
            interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())
            val inferenceTime = System.currentTimeMillis() - startTime
            Log.d(TAG, "Inference completed in ${inferenceTime}ms")

            // Read output properly
            outputBuffer.buffer.rewind()  // Ensure buffer is at the beginning

            val probabilities = when (outputDataType) {
                DataType.FLOAT32 -> {
                    val floatArray = FloatArray(outputSize)
                    outputBuffer.buffer.asFloatBuffer().get(floatArray)
                    Log.d(TAG, "Got ${floatArray.size} float probabilities")
                    floatArray
                }
                DataType.UINT8 -> {
                    val byteArray = ByteArray(outputSize)
                    outputBuffer.buffer.get(byteArray)
                    val floatArray = byteArray.map { (it.toInt() and 0xFF) / 255f }.toFloatArray()
                    Log.d(TAG, "Converted ${byteArray.size} bytes to ${floatArray.size} float probabilities")
                    Log.d(TAG, "Raw bytes: ${byteArray.map { it.toInt() and 0xFF }.toList()}")
                    floatArray
                }
                else -> {
                    Log.w(TAG, "Unexpected output data type: $outputDataType")
                    FloatArray(outputSize)
                }
            }

            // Log raw probabilities
            Log.d(TAG, "Raw probabilities: ${probabilities.contentToString()}")

            // Since this model has only 2 outputs, it's likely a binary classifier
            // Interpret as: [safe_probability, unsafe_probability]
            val isInappropriate: Boolean
            val score: Float
            val categoryScores = mutableMapOf<Category, Float>()

            if (outputSize == 2) {
                // Binary model: assume index 0 is safe, index 1 is unsafe
                val safeProb = probabilities[0]
                val unsafeProb = probabilities[1]

                Log.d(TAG, "Binary model detected:")
                Log.d(TAG, "  Safe probability: $safeProb")
                Log.d(TAG, "  Unsafe probability: $unsafeProb")

                score = unsafeProb
                isInappropriate = unsafeProb > ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD

                // Map to simplified categories
                categoryScores[Category.NEUTRAL] = safeProb
                categoryScores[Category.PORN] = unsafeProb * 0.5f  // Distribute unsafe score
                categoryScores[Category.SEXY] = unsafeProb * 0.3f
                categoryScores[Category.HENTAI] = unsafeProb * 0.2f
                categoryScores[Category.DRAWING] = 0f

            } else if (outputSize >= 5) {
                // Original 5-category model
                Category.entries.forEach { category ->
                    if (category.index < probabilities.size) {
                        val prob = probabilities[category.index]
                        categoryScores[category] = prob
                        Log.d(TAG, "${category.name}: $prob")
                    }
                }

                val pornScore = categoryScores[Category.PORN] ?: 0f
                val sexyScore = categoryScores[Category.SEXY] ?: 0f
                val hentaiScore = categoryScores[Category.HENTAI] ?: 0f

                score = pornScore + sexyScore + hentaiScore
                isInappropriate = score > ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD

            } else {
                Log.w(TAG, "Unexpected output size: $outputSize")
                score = 0f
                isInappropriate = false
            }

            Log.d(TAG, "Final score: $score")
            Log.d(TAG, "Threshold: ${ImageModerationProcessor.DEFAULT_CONTENT_THRESHOLD}")
            Log.d(TAG, "Is inappropriate: $isInappropriate")

            ContentResult(
                isInappropriate = isInappropriate,
                score = score,
                categoryScores = categoryScores
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting content", e)
            e.printStackTrace()
            ContentResult(
                isInappropriate = false,
                score = 0f,
                categoryScores = emptyMap()
            )
        }
    }

    fun close() {
        Log.d(TAG, "Closing ContentDetectionModel")
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
) {
    override fun toString(): String {
        return "ContentResult(isInappropriate=$isInappropriate, score=$score, categories=$categoryScores)"
    }
}