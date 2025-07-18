package com.ae.imageharamblur.detection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import kotlin.math.max
import kotlin.math.min

internal class BlazeFaceDetector(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val imageProcessor: ImageProcessor

    // BlazeFace model parameters
    private val inputSize = 128
    private val outputBoxesShape = intArrayOf(1, 896, 16)
    private val outputScoresShape = intArrayOf(1, 896, 1)

    // Detection parameters
    private val scoreThreshold = 0.5f
    private val iouThreshold = 0.3f

    init {
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(inputSize, inputSize, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(127.5f, 127.5f)) // Normalize to [-1, 1]
            .build()

        loadModel()
    }

    private fun loadModel() {
        try {
            val modelBuffer = FileUtil.loadMappedFile(context, "blaze_face_short_range.tflite")
            val options = Interpreter.Options().apply {
                setNumThreads(4)
                setUseNNAPI(false) // Disable NNAPI for better compatibility
            }
            interpreter = Interpreter(modelBuffer, options)
            Log.d("BlazeFaceDetector", "Model loaded successfully")
        } catch (e: Exception) {
            Log.e("BlazeFaceDetector", "Failed to load model", e)
        }
    }

    fun detectFaces(bitmap: Bitmap): List<DetectedFace> {
        val interpreter = this.interpreter ?: run {
            Log.e("BlazeFaceDetector", "Interpreter not initialized")
            return emptyList()
        }

        try {
            // Prepare input
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            val processedImage = imageProcessor.process(tensorImage)
            val inputBuffer = processedImage.buffer

            // Prepare outputs
            val outputBoxes = Array(1) { Array(896) { FloatArray(16) } }
            val outputScores = Array(1) { Array(896) { FloatArray(1) } }

            // Run inference
            interpreter.runForMultipleInputsOutputs(
                arrayOf(inputBuffer),
                mapOf(0 to outputBoxes, 1 to outputScores)
            )

            // Process detections
            return processDetections(
                outputBoxes[0],
                outputScores[0],
                bitmap.width,
                bitmap.height
            )
        } catch (e: Exception) {
            Log.e("BlazeFaceDetector", "Error during face detection", e)
            return emptyList()
        }
    }

    private fun processDetections(
        boxes: Array<FloatArray>,
        scores: Array<FloatArray>,
        imageWidth: Int,
        imageHeight: Int
    ): List<DetectedFace> {
        val detections = mutableListOf<RawDetection>()

        // Collect valid detections
        for (i in scores.indices) {
            val score = scores[i][0]
            if (score >= scoreThreshold) {
                val box = boxes[i]
                // BlazeFace outputs: [xCenter, yCenter, width, height, ...]
                val xCenter = box[0]
                val yCenter = box[1]
                val width = box[2]
                val height = box[3]

                detections.add(
                    RawDetection(
                        score = score,
                        xCenter = xCenter,
                        yCenter = yCenter,
                        width = width,
                        height = height
                    )
                )
            }
        }

        // Apply NMS
        val nmsResults = nonMaxSuppression(detections)

        // Convert to screen coordinates
        return nmsResults.map { detection ->
            val halfWidth = detection.width * 0.5f
            val halfHeight = detection.height * 0.5f

            val left = max(0, ((detection.xCenter - halfWidth) * imageWidth).toInt())
            val top = max(0, ((detection.yCenter - halfHeight) * imageHeight).toInt())
            val right = min(imageWidth, ((detection.xCenter + halfWidth) * imageWidth).toInt())
            val bottom = min(imageHeight, ((detection.yCenter + halfHeight) * imageHeight).toInt())

            DetectedFace(
                boundingBox = Rect(left, top, right, bottom),
                confidence = detection.score
            )
        }
    }

    private fun nonMaxSuppression(detections: List<RawDetection>): List<RawDetection> {
        if (detections.isEmpty()) return emptyList()

        val sorted = detections.sortedByDescending { it.score }
        val selected = mutableListOf<RawDetection>()
        val active = BooleanArray(sorted.size) { true }

        for (i in sorted.indices) {
            if (!active[i]) continue

            selected.add(sorted[i])

            for (j in i + 1 until sorted.size) {
                if (!active[j]) continue

                val iou = calculateIoU(sorted[i], sorted[j])
                if (iou > iouThreshold) {
                    active[j] = false
                }
            }
        }

        return selected
    }

    private fun calculateIoU(a: RawDetection, b: RawDetection): Float {
        val aLeft = a.xCenter - a.width * 0.5f
        val aRight = a.xCenter + a.width * 0.5f
        val aTop = a.yCenter - a.height * 0.5f
        val aBottom = a.yCenter + a.height * 0.5f

        val bLeft = b.xCenter - b.width * 0.5f
        val bRight = b.xCenter + b.width * 0.5f
        val bTop = b.yCenter - b.height * 0.5f
        val bBottom = b.yCenter + b.height * 0.5f

        val interLeft = max(aLeft, bLeft)
        val interTop = max(aTop, bTop)
        val interRight = min(aRight, bRight)
        val interBottom = min(aBottom, bBottom)

        val interArea = max(0f, interRight - interLeft) * max(0f, interBottom - interTop)
        val aArea = a.width * a.height
        val bArea = b.width * b.height
        val unionArea = aArea + bArea - interArea

        return if (unionArea > 0) interArea / unionArea else 0f
    }

    fun close() {
        interpreter?.close()
        interpreter = null
    }

    data class DetectedFace(
        val boundingBox: Rect,
        val confidence: Float = 0f
    )

    private data class RawDetection(
        val score: Float,
        val xCenter: Float,
        val yCenter: Float,
        val width: Float,
        val height: Float
    )
}