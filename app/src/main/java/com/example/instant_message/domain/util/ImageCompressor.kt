package com.example.instant_message.domain.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object ImageCompressor {

    //压缩图片
    fun compressImage(
        filePath: String,
        maxWidth: Int = 800,
        maxHeight: Int = 800,
        quality: Int = 80)
    : ByteArray {
        //解码
        val originalBitmap = BitmapFactory.decodeFile(filePath)
        ?: throw IllegalArgumentException("Failed to decode image from file")

        //缩放
        val resizedBitmap = resizeBitmap(originalBitmap, maxWidth, maxHeight)

        return try {
            //压缩成jpeg
            val outputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.toByteArray().also { outputStream.close() }
        }catch (e: Exception){
            throw RuntimeException("Failed to compress image",e)
        }

    }

    private fun resizeBitmap(originalBitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        val width = originalBitmap.width
        val height = originalBitmap.height

        if(width <= maxWidth && height <= maxHeight) return originalBitmap

        val ratio = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        val newWidth = (width * ratio).toInt()
        val newHeight = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true)
    }
}