package com.ae.imageharamblur.utils

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.runtime.Stable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Stable
internal suspend fun blurBitmap(input: Bitmap, radius: Int): Bitmap =
    withContext(Dispatchers.Default) {
        val w = input.width
        val h = input.height
        val pixels = IntArray(w * h)
        input.getPixels(pixels, 0, w, 0, 0, w, h)

        val result = pixels.copyOf()
        val div = radius * 2 + 1

        for (y in 0 until h) {
            var rSum = 0
            var gSum = 0
            var bSum = 0

            for (i in -radius..radius) {
                val x = (i.coerceIn(0, w - 1))
                val c = pixels[y * w + x]
                rSum += Color.red(c)
                gSum += Color.green(c)
                bSum += Color.blue(c)
            }

            for (x in 0 until w) {
                val idx = y * w + x
                result[idx] = Color.rgb(rSum / div, gSum / div, bSum / div)

                val i1 = (x - radius).coerceIn(0, w - 1)
                val i2 = (x + radius + 1).coerceIn(0, w - 1)

                val c1 = pixels[y * w + i1]
                val c2 = pixels[y * w + i2]

                rSum += Color.red(c2) - Color.red(c1)
                gSum += Color.green(c2) - Color.green(c1)
                bSum += Color.blue(c2) - Color.blue(c1)
            }
        }

        for (x in 0 until w) {
            var rSum = 0
            var gSum = 0
            var bSum = 0

            for (i in -radius..radius) {
                val y = i.coerceIn(0, h - 1)
                val c = result[y * w + x]
                rSum += Color.red(c)
                gSum += Color.green(c)
                bSum += Color.blue(c)
            }

            for (y in 0 until h) {
                val idx = y * w + x
                pixels[idx] = Color.rgb(rSum / div, gSum / div, bSum / div)

                val i1 = (y - radius).coerceIn(0, h - 1)
                val i2 = (y + radius + 1).coerceIn(0, h - 1)

                val c1 = result[i1 * w + x]
                val c2 = result[i2 * w + x]

                rSum += Color.red(c2) - Color.red(c1)
                gSum += Color.green(c2) - Color.green(c1)
                bSum += Color.blue(c2) - Color.blue(c1)
            }
        }

        Bitmap.createBitmap(pixels, w, h, input.config)
    }