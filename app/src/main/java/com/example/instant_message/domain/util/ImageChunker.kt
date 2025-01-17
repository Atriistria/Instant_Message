package com.example.instant_message.domain.util

object ImageChunker {
    fun chunkImage(imageData: ByteArray, chunkSize: Int): List<ByteArray> {
        val chunks = mutableListOf<ByteArray>()
        var start = 0
        while (start < imageData.size) {
            val end = minOf(start + chunkSize, imageData.size)
            chunks.add(imageData.copyOfRange(start, end))
            start = end
        }
        return chunks
    }
}