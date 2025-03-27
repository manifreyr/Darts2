package `is`.hi.darts2.network

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class GameWebSocketListener(private val onMessageReceived: (String) -> Unit) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("Websocket", "WebSocket opened: ${response.message}")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageReceived(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        onMessageReceived(bytes.utf8())
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        webSocket.close(code, reason)
        Log.d("Websocket", "WebSocket closing: $code / $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("Websocket", "WebSocket error: ${t.localizedMessage}")
    }
}
