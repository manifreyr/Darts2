package `is`.hi.darts2.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import okhttp3.Interceptor
import okhttp3.Response

class CsrfInterceptor(private val cookieJar: PersistentCookieJar) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.method != "GET" && originalRequest.method != "HEAD") {
            val cookies = cookieJar.loadForRequest(originalRequest.url)
            val csrfToken = cookies.find { it.name == "XSRF-TOKEN" }?.value
            if (!csrfToken.isNullOrEmpty()) {
                val newRequest = originalRequest.newBuilder()
                    .addHeader("X-CSRF-TOKEN", csrfToken)
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return chain.proceed(originalRequest)
    }
}
