package cu.video.app.streamingcuba.api

import cu.video.app.streamingcuba.data.models.response.ErrorResponse
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder().url(originalRequest.url).build()
        return try {
            chain.proceed(request)
        } catch (e: Exception) {

            val error = Response.Builder()
            error.protocol(Protocol.HTTP_1_0)
            error.request(originalRequest)
            error.code(500)
            error.body(ErrorResponse())
            error.message("Error: " + e.message)

            error.build()
        }
    }
}
