package ru.barinov.notes.core

import android.graphics.*

suspend fun Bitmap.scaledImageFromBitmap(image: Bitmap, baseImageScale: Int, defaultScale: Float ): Bitmap{
    val pixels = (baseImageScale * defaultScale + 0.5f).toInt()
    return Bitmap.createScaledBitmap(image, pixels, pixels,true)
}