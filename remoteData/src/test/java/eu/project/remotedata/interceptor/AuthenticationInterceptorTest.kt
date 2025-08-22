package eu.project.remotedata.interceptor

import com.google.gson.annotations.SerializedName
import eu.project.remotedata.BuildConfig
import junit.framework.TestCase.assertEquals
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class AuthenticationInterceptorTest {

    private val devOnlyApiKey = BuildConfig.SIAAPI_DEV_ONLY_KEY

    private lateinit var mockWebServer: MockWebServer

    private lateinit var interceptor: AuthenticationInterceptor
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit

    private lateinit var testApiImpl: TestApi

    @Before
    fun setUp() {

        interceptor = AuthenticationInterceptor()
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        testApiImpl = retrofit.create(TestApi::class.java)
    }

    @After
    fun tearDown() {

        mockWebServer.close()
    }

    @Test
    fun `should add x-api-key header to every request`() {

        mockWebServer.enqueue(MockResponse().setBody("{\"value\": \"Cool value :D\"}"))

        // run
        testApiImpl.test().execute()

        // test
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(devOnlyApiKey, recordedRequest.headers["x-api-key"])
    }

    private interface TestApi {

        @GET("test")
        fun test(): Call<TestData>
    }

    private data class TestData(@SerializedName("value") val value: String)
}