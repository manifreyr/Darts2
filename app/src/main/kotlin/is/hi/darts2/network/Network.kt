package `is`.hi.darts2.network

import GameWebSocketListener
import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.Request
import okhttp3.WebSocket

object Network {
    private const val BASE_URL = "http://10.0.2.2:8081/"

    private const val WEBSOCKET_BASE_URL = "ws://10.0.2.2:8081/game-websocket/"

    private lateinit var cookieJar: PersistentCookieJar
    private lateinit var client: OkHttpClient
    private lateinit var retrofit: Retrofit

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }


    fun init(context: Context) {
        cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
        client = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    /**
     * Creates a websocket connection for game updates.
     * The URL is constructed to include the current user id.
     *
     * @param onMessageReceived Callback invoked when a message is received.
     * @return A [WebSocket] instance.
     */
    fun createGameWebSocket(onMessageReceived: (String) -> Unit): WebSocket {
        val url = WEBSOCKET_BASE_URL
        val request = Request.Builder().url(url).build()
        val listener = GameWebSocketListener(onMessageReceived)
        return client.newWebSocket(request, listener)
    }
}
