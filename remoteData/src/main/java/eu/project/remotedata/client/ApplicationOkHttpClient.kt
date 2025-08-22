package eu.project.remotedata.client

import eu.project.remotedata.interceptor.AuthenticationInterceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ApplicationOkHttpClient @Inject constructor(authenticationInterceptor: AuthenticationInterceptor) {

    val client = OkHttpClient.Builder()
        .addInterceptor(authenticationInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
}