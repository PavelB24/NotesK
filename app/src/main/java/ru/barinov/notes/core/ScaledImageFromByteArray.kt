package ru.barinov.notes.core

import android.graphics.*
import kotlin.coroutines.*

suspend fun Bitmap.scaledImageFromBitmap(image: Bitmap, baseImageScale: Int, defaultScale: Float ): Bitmap =
    suspendCoroutine { continuation ->
        val pixels = (baseImageScale * defaultScale + 0.5f).toInt()
        continuation.resume( Bitmap.createScaledBitmap(image, pixels, pixels, true))
    }
