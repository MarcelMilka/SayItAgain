package eu.project.remotedata.interceptor

import eu.project.remotedata.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AuthenticationInterceptor @Inject constructor(): Interceptor {

    private val devOnlyApiKey = BuildConfig.SIAAPI_DEV_ONLY_KEY

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("x-api-key", devOnlyApiKey)
            .build()

        return chain.proceed(modifiedRequest)
    }
}