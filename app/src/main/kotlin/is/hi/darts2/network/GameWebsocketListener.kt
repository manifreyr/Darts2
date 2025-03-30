import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class GameWebSocketListener(
    private val onMessageReceived: (String) -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("GameWebSocket", "Connected to the WebSocket!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("GameWebSocket", "Received message: $text")
        onMessageReceived(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("GameWebSocket", "Closing WebSocket: code=$code, reason=$reason")
        webSocket.close(code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("GameWebSocket", "WebSocket closed: code=$code, reason=$reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("GameWebSocket", "WebSocket error: ${t.message}")
    }
}
