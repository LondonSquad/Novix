package com.ae.imageharamblur.detection

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.common.ops.NormalizeOp
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.max
import kotlin.math.min

class FaceDetector(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val imageProcessor: ImageProcessor

    init {
        Log.d(TAG, "FaceDetector initializing...")
        loadModel()

        // Create image processor with normalization
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(INPUT_SIZE, INPUT_SIZE, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(127.5f, 127.5f)) // Normalize to [-1, 1]
            .build()

        Log.d(TAG, "FaceDetector initialized with input size: $INPUT_SIZE")
    }

    private fun loadModel() {
        try {
            Log.d(TAG, "Loading model: $MODEL_FILE")
            val modelBuffer = FileUtil.loadMappedFile(context, MODEL_FILE)
            Log.d(TAG, "Model file loaded, size: ${modelBuffer.capacity()} bytes")

            val options = Interpreter.Options().apply {
                setNumThreads(4)
            }

            interpreter?.close()
            interpreter = Interpreter(modelBuffer, options)

            // Log model details
            interpreter?.let { interp ->
                Log.d(TAG, "Model input count: ${interp.inputTensorCount}")
                Log.d(TAG, "Model output count: ${interp.outputTensorCount}")

                for (i in 0 until interp.inputTensorCount) {
                    val tensor = interp.getInputTensor(i)
                    Log.d(TAG, "Input $i: shape=${tensor.shape().contentToString()}, dataType=${tensor.dataType()}")
                }

                for (i in 0 until interp.outputTensorCount) {
                    val tensor = interp.getOutputTensor(i)
                    Log.d(TAG, "Output $i: shape=${tensor.shape().contentToString()}, dataType=${tensor.dataType()}")
                }
            }

            Log.d(TAG, "Model loaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error loading model", e)
        }
    }

    fun detectFaces(bitmap: Bitmap): List<DetectedFace> {
        Log.d(TAG, "detectFaces called with bitmap: ${bitmap.width}x${bitmap.height}")

        val interpreter = this.interpreter
        if (interpreter == null) {
            Log.e(TAG, "Interpreter is null, returning empty list")
            return emptyList()
        }

        try {
            // Scale bitmap to model input size
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, true)

            // Allocate input buffer
            val inputBuffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * NUM_CHANNELS * 4)
            inputBuffer.order(ByteOrder.nativeOrder())

            // Convert bitmap to float array
            val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
            scaledBitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

            for (pixel in pixels) {
                val r = (pixel shr 16 and 0xFF)
                val g = (pixel shr 8 and 0xFF)
                val b = (pixel and 0xFF)

                // Normalize pixel values to [-1, 1]
                inputBuffer.putFloat((r - 127.5f) / 127.5f)
                inputBuffer.putFloat((g - 127.5f) / 127.5f)
                inputBuffer.putFloat((b - 127.5f) / 127.5f)
            }

            inputBuffer.rewind()
            Log.d(TAG, "Input buffer prepared: ${inputBuffer.remaining()} bytes")

            // Prepare outputs - matching model's actual output shape
            val regressionOutput = Array(1) { Array(NUM_ANCHORS) { FloatArray(16) } }
            val classificationOutput = Array(1) { Array(NUM_ANCHORS) { FloatArray(1) } }

            val outputs = mapOf(
                0 to regressionOutput,
                1 to classificationOutput
            )

            // Run inference
            Log.d(TAG, "Running inference...")
            val startTime = System.currentTimeMillis()

            interpreter.runForMultipleInputsOutputs(
                arrayOf(inputBuffer),
                outputs
            )

            val inferenceTime = System.currentTimeMillis() - startTime
            Log.d(TAG, "Inference completed in ${inferenceTime}ms")

            // Check output values
            var maxScore = 0f
            var scoreAboveThreshold = 0
            for (i in 0 until NUM_ANCHORS) {
                val score = sigmoid(classificationOutput[0][i][0])
                if (score > maxScore) maxScore = score
                if (score > CONFIDENCE_THRESHOLD) scoreAboveThreshold++
            }
            Log.d(TAG, "Max confidence score: $maxScore")
            Log.d(TAG, "Anchors with score > $CONFIDENCE_THRESHOLD: $scoreAboveThreshold")

            // Post-process results
            val detections = postProcessResults(
                regressionOutput[0],
                classificationOutput[0],
                bitmap.width,
                bitmap.height
            )

            Log.d(TAG, "Post-processing complete. Detected ${detections.size} faces")
            return detections

        } catch (e: Exception) {
            Log.e(TAG, "Error during detection", e)
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun postProcessResults(
        regression: Array<FloatArray>,
        classification: Array<FloatArray>,
        imageWidth: Int,
        imageHeight: Int
    ): List<DetectedFace> {
        Log.d(TAG, "postProcessResults: imageSize=${imageWidth}x${imageHeight}")

        val detections = mutableListOf<DetectedFace>()
        val anchors = generateAnchors()

        Log.d(TAG, "Generated ${anchors.size} anchors")

        for (i in regression.indices) {
            val score = sigmoid(classification[i][0])

            if (score > CONFIDENCE_THRESHOLD) {
                val anchor = anchors[i]
                val box = decodeBox(regression[i], anchor)

                // Convert normalized coordinates to pixel coordinates
                val rect = Rect(
                    (box.left * imageWidth).toInt().coerceIn(0, imageWidth),
                    (box.top * imageHeight).toInt().coerceIn(0, imageHeight),
                    (box.right * imageWidth).toInt().coerceIn(0, imageWidth),
                    (box.bottom * imageHeight).toInt().coerceIn(0, imageHeight)
                )

                // Validate rect
                if (rect.width() > 10 && rect.height() > 10) { // Minimum face size
                    detections.add(
                        DetectedFace(
                            boundingBox = rect,
                            confidence = score
                        )
                    )
                    Log.d(TAG, "Added detection: rect=$rect, confidence=$score")
                } else {
                    Log.w(TAG, "Skipped small/invalid rect: $rect")
                }
            }
        }

        Log.d(TAG, "Before NMS: ${detections.size} detections")

        // Apply Non-Maximum Suppression
        val nmsResult = nonMaximumSuppression(detections)

        Log.d(TAG, "After NMS: ${nmsResult.size} detections")

        return nmsResult
    }

    private fun generateAnchors(): List<Anchor> {
        val anchors = mutableListOf<Anchor>()

        // For 256x256 input with 896 anchors, this is likely the front/short-range model configuration
        // MediaPipe front camera model uses these strides
        val strides = intArrayOf(8, 16, 16, 16)
        val anchorCounts = intArrayOf(2, 6, 6, 6)

        var totalAnchors = 0

        for (layerId in strides.indices) {
            val stride = strides[layerId]
            val gridSize = INPUT_SIZE / stride
            val anchorCount = anchorCounts[layerId]

            Log.v(TAG, "Layer $layerId: stride=$stride, gridSize=$gridSize, anchorsPerCell=$anchorCount")

            for (gridY in 0 until gridSize) {
                for (gridX in 0 until gridSize) {
                    for (n in 0 until anchorCount) {
                        val x = (gridX + 0.5f) / gridSize
                        val y = (gridY + 0.5f) / gridSize

                        anchors.add(Anchor(x, y, 1f, 1f))
                        totalAnchors++
                    }
                }
            }
        }

        Log.d(TAG, "Generated $totalAnchors anchors (expected: $NUM_ANCHORS)")

        // Make sure we have exactly 896 anchors
        return anchors.take(NUM_ANCHORS)
    }

    private fun decodeBox(raw: FloatArray, anchor: Anchor): RectF {
        // MediaPipe uses a specific box decoding format
        val cx = raw[0] / INPUT_SIZE * anchor.width + anchor.x
        val cy = raw[1] / INPUT_SIZE * anchor.height + anchor.y
        val w = raw[2] / INPUT_SIZE * anchor.width
        val h = raw[3] / INPUT_SIZE * anchor.height

        val left = (cx - w * 0.5f).coerceIn(0f, 1f)
        val top = (cy - h * 0.5f).coerceIn(0f, 1f)
        val right = (cx + w * 0.5f).coerceIn(0f, 1f)
        val bottom = (cy + h * 0.5f).coerceIn(0f, 1f)

        return RectF(left, top, right, bottom)
    }

    private fun nonMaximumSuppression(
        detections: List<DetectedFace>,
        iouThreshold: Float = 0.3f
    ): List<DetectedFace> {
        if (detections.isEmpty()) return emptyList()

        Log.d(TAG, "NMS: processing ${detections.size} detections with IoU threshold $iouThreshold")

        val sorted = detections.sortedByDescending { it.confidence }
        val selected = mutableListOf<DetectedFace>()

        for (detection in sorted) {
            var shouldSelect = true

            for (selectedDetection in selected) {
                val iou = calculateIoU(detection.boundingBox, selectedDetection.boundingBox)
                if (iou > iouThreshold) {
                    Log.v(TAG, "NMS: Suppressing detection with IoU=$iou")
                    shouldSelect = false
                    break
                }
            }

            if (shouldSelect) {
                selected.add(detection)
                Log.d(TAG, "NMS: Selected detection with confidence=${detection.confidence}")
            }
        }

        return selected
    }

    private fun calculateIoU(box1: Rect, box2: Rect): Float {
        val intersectionLeft = max(box1.left, box2.left)
        val intersectionTop = max(box1.top, box2.top)
        val intersectionRight = min(box1.right, box2.right)
        val intersectionBottom = min(box1.bottom, box2.bottom)

        val intersectionArea = max(0, intersectionRight - intersectionLeft) *
                max(0, intersectionBottom - intersectionTop)

        val box1Area = box1.width() * box1.height()
        val box2Area = box2.width() * box2.height()

        val unionArea = box1Area + box2Area - intersectionArea

        return if (unionArea > 0) intersectionArea.toFloat() / unionArea else 0f
    }

    private fun sigmoid(x: Float): Float = 1f / (1f + kotlin.math.exp(-x))

    fun close() {
        Log.d(TAG, "Closing FaceDetector...")
        interpreter?.close()
        interpreter = null
    }

    data class DetectedFace(
        val boundingBox: Rect,
        val confidence: Float
    )

    private data class Anchor(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float
    )


    companion object {
        private const val TAG = "FaceDetector"
        private const val CONFIDENCE_THRESHOLD = 0.5f
        private const val MODEL_FILE = "face_detection_back.tflite"
        private const val INPUT_SIZE = 256
        private const val NUM_ANCHORS = 896
        private const val NUM_CHANNELS = 3
    }

}