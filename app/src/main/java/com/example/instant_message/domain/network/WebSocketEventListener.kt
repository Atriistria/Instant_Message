package com.example.instant_message.domain.network

import okhttp3.WebSocket
import okio.ByteString

interface  WebSocketEventListener {
    fun onOpen(webSocket: WebSocket)
    fun onMessage(message: String)
    fun onMessage(byteString: ByteString)
    fun onClosed()
    fun onFailure(error: Throwable)
}