package `fun`.inaction.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RequestLogInterceptor:Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.v("network",request.toLogString())
        return chain.proceed(request)
    }
}