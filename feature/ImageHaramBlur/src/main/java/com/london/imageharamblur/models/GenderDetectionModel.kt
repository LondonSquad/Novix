package com.london.imageharamblur.models

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.graphics.createBitmap
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
import kotlin.math.exp

internal class GenderDetectionModel {
    private val interpreter: Interpreter
    private val imageProcessor: ImageProcessor
    private val inputSize: Int
    private val inputDataType: DataType

    constructor(context: Context) {
        val modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)
        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputSize = if (inputShape.size >= 3) inputShape[1] else INPUT_SIZE
        this.inputDataType = inputTensor.dataType()
        this.imageProcessor = createImageProcessor()
    }

    constructor(modelFile: File) {
        val modelBuffer = loadModelFile(modelFile)
        this.interpreter = createInterpreter(modelBuffer)

        val inputTensor = interpreter.getInputTensor(0)
        val inputShape = inputTensor.shape()
        this.inputSize = if (inputShape.size >= 3) inputShape[1] else INPUT_SIZE
        this.inputDataType = inputTensor.dataType()
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
        }
        return Interpreter(modelBuffer, options)
    }

    private fun createImageProcessor(): ImageProcessor {
        val builder = ImageProcessor.Builder()
            .add(ResizeOp(inputSize, inputSize, ResizeOp.ResizeMethod.BILINEAR))

        when (inputDataType) {
            DataType.UINT8 -> {
                builder.add(NormalizeOp(0f, 1f))
            }

            DataType.FLOAT32 -> {
                builder.add(NormalizeOp(IMAGE_MEAN, IMAGE_STD))
            }

            else -> {
                builder.add(NormalizeOp(0f, 255f))
            }
        }

        return builder.build()
    }

    fun detectGender(faceBitmap: Bitmap): GenderResult {
        return try {
            val rgbBitmap = ensureRgbBitmap(faceBitmap)
            val tensorImage = TensorImage(inputDataType)
            tensorImage.load(rgbBitmap)

            val processedImage = imageProcessor.process(tensorImage)

            val outputTensor = interpreter.getOutputTensor(0)
            val outputShape = outputTensor.shape()
            val outputDataType = outputTensor.dataType()

            val outputBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType)

            synchronized(interpreter) {
                interpreter.run(processedImage.buffer, outputBuffer.buffer.rewind())
            }

            val (femaleProbability, maleProbability) = when (outputDataType) {
                DataType.FLOAT32 -> {
                    val floatArray = outputBuffer.floatArray
                    if (floatArray.size >= 2) {
                        floatArray[FEMALE_INDEX] to floatArray[MALE_INDEX]
                    } else {
                        Log.e("GenderModel", "Unexpected output size: ${floatArray.size}")
                        0.5f to 0.5f
                    }
                }

                DataType.UINT8 -> {
                    val byteArray = ByteArray(outputBuffer.buffer.remaining())
                    outputBuffer.buffer.get(byteArray)
                    if (byteArray.size >= 2) {
                        val femaleProb = (byteArray[FEMALE_INDEX].toInt() and 0xFF) / 255f
                        val maleProb = (byteArray[MALE_INDEX].toInt() and 0xFF) / 255f
                        femaleProb to maleProb
                    } else {
                        0.5f to 0.5f
                    }
                }

                else -> {
                    0.5f to 0.5f
                }
            }

            val maxProb = maxOf(femaleProbability, maleProbability)
            val expFemale = exp(femaleProbability - maxProb)
            val expMale = exp(maleProbability - maxProb)
            val sumExp = expFemale + expMale

            val normalizedFemaleProbability = (expFemale / sumExp)
            val normalizedMaleProbability = (expMale / sumExp)

            val isFemale = normalizedFemaleProbability > normalizedMaleProbability
            val confidence =
                if (isFemale) normalizedFemaleProbability else normalizedMaleProbability

            GenderResult(
                isFemale = isFemale,
                confidence = confidence
            )
        } catch (_: Exception) {
            GenderResult(
                isFemale = false,
                confidence = 0.5f
            )
        }
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

    companion object {
        private const val MODEL_FILE = "gender_class_model.tflite"
        private const val INPUT_SIZE = 224
        private const val IMAGE_MEAN = 127.5f
        private const val IMAGE_STD = 127.5f
        private const val FEMALE_INDEX = 0
        private const val MALE_INDEX = 1
    }
}

internal data class GenderResult(
    val isFemale: Boolean,
    val confidence: Float
)