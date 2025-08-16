package com.london.imageharamblur.faceDetection

import android.content.Context
import android.graphics.*
import androidx.core.graphics.get
import androidx.core.graphics.scale
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class FaceDetector(private val context: Context) {

    // Thread-local interpreters for thread safety
    private val interpreterThreadLocal = ThreadLocal<Interpreter?>()
    private val interpreterMutex = Mutex()
    private val anchors: List<Anchor> by lazy { generateAnchors() }

    private data class Anchor(val x: Float, val y: Float)

    private val modelBuffer by lazy {
        runCatching {
            FileUtil.loadMappedFile(context, MODEL_FILE)
        }.getOrNull()
    }

    init {
        setupModel()
    }

    private fun setupModel() {
        runCatching {
            // Pre-load model buffer for validation
            modelBuffer
        }
    }

    private fun getOrCreateInterpreter(): Interpreter? {
        var interpreter = interpreterThreadLocal.get()
        if (interpreter == null && modelBuffer != null) {
            runCatching {
                val options = Interpreter.Options().apply {
                    setNumThreads(1) // Each thread gets its own interpreter with 1 thread
                    runCatching {
                        setUseXNNPACK(true)
                    }
                }
                interpreter = Interpreter(modelBuffer!!, options)
                interpreterThreadLocal.set(interpreter)
            }.onFailure {
                return null
            }
        }
        return interpreter
    }

    private fun generateAnchors(): List<Anchor> {
        return runCatching {
            val anchorsList = mutableListOf<Anchor>()

            val gridSize1 = 16
            for (y in 0 until gridSize1) {
                for (x in 0 until gridSize1) {
                    val cx = (x + 0.5f) / gridSize1
                    val cy = (y + 0.5f) / gridSize1
                    repeat(2) {
                        anchorsList.add(Anchor(cx, cy))
                    }
                }
            }

            val gridSize2 = 8
            for (y in 0 until gridSize2) {
                for (x in 0 until gridSize2) {
                    val cx = (x + 0.5f) / gridSize2
                    val cy = (y + 0.5f) / gridSize2
                    repeat(6) {
                        anchorsList.add(Anchor(cx, cy))
                    }
                }
            }

            anchorsList
        }.getOrElse { emptyList() }
    }

    suspend fun detectFaces(bitmap: Bitmap): List<DetectedFace> = withContext(Dispatchers.IO) {
        interpreterMutex.withLock {
            runCatching {
                val interpreter = getOrCreateInterpreter() ?: return@withContext emptyList()

                val allFaces = mutableListOf<DetectedFace>()

                // Strategy 1: Original image
                runCatching {
                    val originalFaces = detectFacesInternal(bitmap, interpreter, 1.0f)
                    allFaces.addAll(originalFaces)
                }

                // Strategy 2: Enhanced contrast (helps with difficult images)
                val needsEnhancement = runCatching {
                    needsEnhancement(bitmap)
                }.getOrElse { false }

                if (allFaces.isEmpty() || needsEnhancement) {
                    runCatching {
                        val enhanced = enhanceImageForDetection(bitmap)
                        val enhancedFaces = detectFacesInternal(enhanced, interpreter, 0.9f)
                        allFaces.addAll(enhancedFaces)
                        if (enhanced != bitmap) enhanced.recycle()
                    }
                }

                // Strategy 3: Multi-scale detection
                if (bitmap.width > 500 && allFaces.size < 2) {
                    runCatching {
                        val scaleFactor = 0.75f
                        val scaledBitmap = bitmap.scale(
                            (bitmap.width * scaleFactor).toInt(),
                            (bitmap.height * scaleFactor).toInt()
                        )

                        val scaledFaces = detectFacesInternal(scaledBitmap, interpreter, 0.95f)
                        scaledFaces.forEach { face ->
                            val adjustedBox = Rect(
                                (face.boundingBox.left / scaleFactor).toInt(),
                                (face.boundingBox.top / scaleFactor).toInt(),
                                (face.boundingBox.right / scaleFactor).toInt(),
                                (face.boundingBox.bottom / scaleFactor).toInt()
                            )
                            allFaces.add(DetectedFace(adjustedBox, face.confidence * 0.95f))
                        }
                        scaledBitmap.recycle()
                    }
                }

                // Apply enhanced NMS
                val finalFaces = runCatching {
                    enhancedNMS(allFaces).take(MAX_FACES)
                }.getOrElse { allFaces.take(MAX_FACES) }

                return@withContext finalFaces

            }.getOrElse { emptyList() }
        }
    }

    private fun detectFacesInternal(
        bitmap: Bitmap,
        interpreter: Interpreter,
        scoreMultiplier: Float = 1.0f
    ): List<DetectedFace> {
        return runCatching {
            val scaledBitmap = runCatching {
                bitmap.scale(INPUT_SIZE, INPUT_SIZE)
            }.getOrElse { return emptyList() }

            val inputBuffer = runCatching {
                bitmapToByteBuffer(scaledBitmap)
            }.getOrElse {
                scaledBitmap.recycle()
                return emptyList()
            }

            scaledBitmap.recycle()

            val regressionOutput = Array(1) { Array(NUM_ANCHORS) { FloatArray(16) } }
            val classificationOutput = Array(1) { Array(NUM_ANCHORS) { FloatArray(1) } }

            val outputs = mapOf(
                0 to regressionOutput,
                1 to classificationOutput
            )

            runCatching {
                interpreter.runForMultipleInputsOutputs(arrayOf(inputBuffer), outputs)
            }.onFailure {
                return emptyList()
            }

            runCatching {
                postProcessResults(
                    regressionOutput[0],
                    classificationOutput[0],
                    bitmap.width,
                    bitmap.height,
                    scoreMultiplier
                )
            }.getOrElse { emptyList() }

        }.getOrElse { emptyList() }
    }

    private fun needsEnhancement(bitmap: Bitmap): Boolean {
        return runCatching {
            val sampleSize = 100
            val step = (bitmap.width * bitmap.height) / sampleSize
            var totalBrightness = 0
            var minBrightness = 255
            var maxBrightness = 0

            for (i in 0 until sampleSize) {
                val x = (i * step) % bitmap.width
                val y = (i * step) / bitmap.width
                if (x < bitmap.width && y < bitmap.height) {
                    runCatching {
                        val pixel = bitmap[x, y]
                        val brightness = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                        totalBrightness += brightness
                        minBrightness = min(minBrightness, brightness)
                        maxBrightness = max(maxBrightness, brightness)
                    }
                }
            }

            val avgBrightness = totalBrightness / sampleSize
            val contrast = maxBrightness - minBrightness

            avgBrightness < 80 || avgBrightness > 200 || contrast < 50
        }.getOrElse { false }
    }

    private fun enhanceImageForDetection(bitmap: Bitmap): Bitmap {
        return runCatching {
            val enhanced = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(enhanced)
            val paint = Paint()

            val colorMatrix = ColorMatrix()
            val contrast = 1.3f
            val brightness = -10f
            val scale = contrast
            val translate = brightness + 128f * (1 - contrast)

            colorMatrix.set(floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            ))

            paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            enhanced
        }.getOrElse { bitmap }
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        return runCatching {
            val inputBuffer = ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * NUM_CHANNELS * 4)
            inputBuffer.order(ByteOrder.nativeOrder())

            val pixels = IntArray(INPUT_SIZE * INPUT_SIZE)
            bitmap.getPixels(pixels, 0, INPUT_SIZE, 0, 0, INPUT_SIZE, INPUT_SIZE)

            for (pixel in pixels) {
                val r = (pixel shr 16 and 0xFF)
                val g = (pixel shr 8 and 0xFF)
                val b = (pixel and 0xFF)

                inputBuffer.putFloat((r - 127.5f) / 127.5f)
                inputBuffer.putFloat((g - 127.5f) / 127.5f)
                inputBuffer.putFloat((b - 127.5f) / 127.5f)
            }

            inputBuffer.rewind()
            inputBuffer
        }.getOrElse {
            // Return empty buffer as fallback
            ByteBuffer.allocateDirect(1 * INPUT_SIZE * INPUT_SIZE * NUM_CHANNELS * 4).apply {
                order(ByteOrder.nativeOrder())
            }
        }
    }

    private fun postProcessResults(
        regression: Array<FloatArray>,
        classification: Array<FloatArray>,
        imageWidth: Int,
        imageHeight: Int,
        scoreMultiplier: Float = 1.0f
    ): List<DetectedFace> {
        return runCatching {
            val detections = mutableListOf<DetectedFace>()

            var maxScore = 0f
            for (i in classification.indices) {
                runCatching {
                    val score = sigmoid(classification[i][0])
                    if (score > maxScore) maxScore = score
                }
            }

            val dynamicThreshold = when {
                maxScore > 0.9f -> CONFIDENCE_THRESHOLD
                maxScore > 0.7f -> CONFIDENCE_THRESHOLD * 0.9f
                maxScore > 0.5f -> CONFIDENCE_THRESHOLD * 0.8f
                else -> CONFIDENCE_THRESHOLD * 0.7f
            }

            for (i in regression.indices) {
                runCatching {
                    val rawScore = sigmoid(classification[i][0])
                    val score = rawScore * scoreMultiplier

                    if (score > dynamicThreshold && i < anchors.size) {
                        val anchor = anchors[i]
                        val raw = regression[i]

                        val cx = raw[0] / INPUT_SIZE + anchor.x
                        val cy = raw[1] / INPUT_SIZE + anchor.y
                        val w = raw[2] / INPUT_SIZE
                        val h = raw[3] / INPUT_SIZE

                        val padding = if (score > 0.8f) 1.05f else 1.1f
                        val paddedW = w * padding
                        val paddedH = h * padding

                        val left = ((cx - paddedW * 0.5f) * imageWidth).coerceIn(0f, imageWidth.toFloat()).toInt()
                        val top = ((cy - paddedH * 0.5f) * imageHeight).coerceIn(0f, imageHeight.toFloat()).toInt()
                        val right = ((cx + paddedW * 0.5f) * imageWidth).coerceIn(0f, imageWidth.toFloat()).toInt()
                        val bottom = ((cy + paddedH * 0.5f) * imageHeight).coerceIn(0f, imageHeight.toFloat()).toInt()

                        val rect = Rect(left, top, right, bottom)

                        val boxWidth = rect.width()
                        val boxHeight = rect.height()

                        if (boxWidth >= MIN_FACE_SIZE && boxHeight >= MIN_FACE_SIZE) {
                            val aspectRatio = boxWidth.toFloat() / boxHeight
                            if (aspectRatio in 0.3f..3.0f) {
                                val centerX = (left + right) / 2f / imageWidth
                                val centerY = (top + bottom) / 2f / imageHeight
                                var adjustedScore = score

                                if (centerX in 0.3f..0.7f && centerY in 0.2f..0.8f) {
                                    adjustedScore *= 1.1f
                                }

                                detections.add(DetectedFace(rect, adjustedScore.coerceIn(0f, 1f)))
                            }
                        }
                    }
                }
            }

            detections
        }.getOrElse { emptyList() }
    }

    private fun enhancedNMS(faces: List<DetectedFace>): List<DetectedFace> {
        return runCatching {
            if (faces.isEmpty()) return emptyList()

            val sorted = faces.sortedByDescending { it.confidence }
            val selected = mutableListOf<DetectedFace>()

            for (face in sorted) {
                runCatching {
                    var shouldSelect = true
                    var bestOverlap: DetectedFace? = null
                    var maxIoU = 0f

                    for (selectedFace in selected) {
                        runCatching {
                            val iou = calculateIoU(face.boundingBox, selectedFace.boundingBox)

                            if (iou > maxIoU) {
                                maxIoU = iou
                                bestOverlap = selectedFace
                            }

                            val adaptiveThreshold = if (face.confidence > 0.8f && selectedFace.confidence > 0.8f) {
                                IOU_THRESHOLD * 0.8f
                            } else {
                                IOU_THRESHOLD
                            }

                            if (iou > adaptiveThreshold) {
                                shouldSelect = false
                            }
                        }
                    }

                    if (!shouldSelect && bestOverlap != null && maxIoU > 0.6f) {
                        if (face.confidence > bestOverlap.confidence * 1.2f) {
                            selected.remove(bestOverlap)
                            selected.add(face)
                        }
                    } else if (shouldSelect) {
                        selected.add(face)
                    }
                }
            }

            selected
        }.getOrElse { faces }
    }

    private fun calculateIoU(box1: Rect, box2: Rect): Float {
        return runCatching {
            val intersectionLeft = max(box1.left, box2.left)
            val intersectionTop = max(box1.top, box2.top)
            val intersectionRight = min(box1.right, box2.right)
            val intersectionBottom = min(box1.bottom, box2.bottom)

            val intersectionArea = max(0, intersectionRight - intersectionLeft) *
                    max(0, intersectionBottom - intersectionTop)

            val box1Area = box1.width() * box1.height()
            val box2Area = box2.width() * box2.height()
            val unionArea = box1Area + box2Area - intersectionArea

            if (unionArea > 0) intersectionArea.toFloat() / unionArea else 0f
        }.getOrElse { 0f }
    }

    private fun sigmoid(x: Float): Float {
        return runCatching {
            1f / (1f + exp(-x))
        }.getOrElse { 0.5f }
    }

    fun close() {
        runCatching {
            interpreterThreadLocal.get()?.close()
            interpreterThreadLocal.remove()
        }
    }

    companion object {
        private const val CONFIDENCE_THRESHOLD = 0.35f
        private const val IOU_THRESHOLD = 0.25f
        private const val MIN_FACE_SIZE = 15
        private const val MAX_FACES = 20
        private const val MODEL_FILE = "face_detection_short_range.tflite"
        private const val INPUT_SIZE = 128
        private const val NUM_ANCHORS = 896
        private const val NUM_CHANNELS = 3
    }
}
