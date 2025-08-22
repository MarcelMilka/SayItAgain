package eu.project.remotedata.client

import eu.project.remotedata.BuildConfig
import eu.project.remotedata.endpoint.ExportEndpoints
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

internal class WebApplicationClientImpl @Inject constructor(okHttpClient: ApplicationOkHttpClient): WebApplicationClient {

    private val baseUrl = BuildConfig.SIAAPI_BASE_URL

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient.client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override val exportEndpoints: ExportEndpoints =
        retrofit.create(ExportEndpoints::class.java)
}