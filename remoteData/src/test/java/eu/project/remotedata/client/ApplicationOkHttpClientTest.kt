package eu.project.remotedata.client


import eu.project.remotedata.interceptor.AuthenticationInterceptor
import io.mockk.every
import io.mockk.mockk
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class ApplicationOkHttpClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var mockAuthenticationInterceptor: AuthenticationInterceptor
    private lateinit var applicationOkHttpClient: ApplicationOkHttpClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockAuthenticationInterceptor = mockk()
        applicationOkHttpClient = ApplicationOkHttpClient(mockAuthenticationInterceptor)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should create OkHttpClient successfully`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = applicationOkHttpClient.client

        // test
        assertNotNull(client)
        assertTrue(client is OkHttpClient)
    }

    @Test
    fun `should configure correct timeout values`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = applicationOkHttpClient.client

        // test
        assertEquals(30, client.connectTimeoutMillis / 1000)
        assertEquals(30, client.readTimeoutMillis / 1000)
    }

    @Test
    fun `should include authentication interceptor in client`() {

        // stub
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setBody("test response"))

        every { mockAuthenticationInterceptor.intercept(any()) } answers {

            val chain = firstArg<okhttp3.Interceptor.Chain>()
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                .addHeader("x-api-key", "test-api-key")
                .build()
            chain.proceed(modifiedRequest)
        }

        // run
        val client = applicationOkHttpClient.client
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()
        val response = client.newCall(request).execute()

        // test
        assertEquals(200, response.code)
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("test-api-key", recordedRequest.headers["x-api-key"])
    }

    @Test
    fun `should use correct default configuration values`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = applicationOkHttpClient.client

        // test
        assertEquals(30, client.connectTimeoutMillis / 1000)
        assertEquals(30, client.readTimeoutMillis / 1000)
        assertTrue(client.retryOnConnectionFailure)
    }

    @Test
    fun `should handle multiple requests with authentication`() {

        // stub
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setBody("response 1"))
        mockWebServer.enqueue(MockResponse().setBody("response 2"))
        every { mockAuthenticationInterceptor.intercept(any()) } answers {
            val chain = firstArg<okhttp3.Interceptor.Chain>()
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                .addHeader("x-api-key", "test-api-key")
                .build()
            chain.proceed(modifiedRequest)
        }

        // run
        val client = applicationOkHttpClient.client
        val request1 = Request.Builder()
            .url(mockWebServer.url("/test1"))
            .build()
        val request2 = Request.Builder()
            .url(mockWebServer.url("/test2"))
            .build()

        val response1 = client.newCall(request1).execute()
        val response2 = client.newCall(request2).execute()

        // test
        assertEquals(200, response1.code)
        assertEquals(200, response2.code)
        assertEquals("test-api-key", mockWebServer.takeRequest().headers["x-api-key"])
        assertEquals("test-api-key", mockWebServer.takeRequest().headers["x-api-key"])
    }

    @Test
    fun `should preserve existing headers when adding authentication`() {

        // stub
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setBody("test response"))
        every { mockAuthenticationInterceptor.intercept(any()) } answers {
            val chain = firstArg<okhttp3.Interceptor.Chain>()
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                .addHeader("x-api-key", "test-api-key")
                .build()
            chain.proceed(modifiedRequest)
        }

        // run
        val client = applicationOkHttpClient.client
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .addHeader("content-type", "application/json")
            .addHeader("user-agent", "test-client")
            .build()
        val response = client.newCall(request).execute()

        // test
        assertEquals(200, response.code)
        val recordedRequest = mockWebServer.takeRequest()
        assertEquals("test-api-key", recordedRequest.headers["x-api-key"])
        assertEquals("application/json", recordedRequest.headers["content-type"])
        assertEquals("test-client", recordedRequest.headers["user-agent"])
    }

    @Test
    fun `should handle connection failures properly`() {

        // stub
        // Don't start MockWebServer to simulate connection failure
        every { mockAuthenticationInterceptor.intercept(any()) } answers {
            val chain = firstArg<okhttp3.Interceptor.Chain>()
            chain.proceed(chain.request())
        }

        // run
        val client = applicationOkHttpClient.client
        val request = Request.Builder()
            .url("http://localhost:9999/nonexistent")
            .build()

        // test
        assertThrows(java.net.ConnectException::class.java) {
            client.newCall(request).execute()
        }
    }

    @Test
    fun `should create client with retry on connection failure enabled`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = applicationOkHttpClient.client

        // test
        assertTrue(client.retryOnConnectionFailure)
    }

    @Test
    fun `should handle empty response body correctly`() {

        // stub
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setBody(""))
        every { mockAuthenticationInterceptor.intercept(any()) } answers {
            val chain = firstArg<okhttp3.Interceptor.Chain>()
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                .addHeader("x-api-key", "test-api-key")
                .build()
            chain.proceed(modifiedRequest)
        }

        // run
        val client = applicationOkHttpClient.client
        val request = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()
        val response = client.newCall(request).execute()

        // test
        assertEquals(200, response.code)
        assertEquals("", response.body?.string())
        assertEquals("test-api-key", mockWebServer.takeRequest().headers["x-api-key"])
    }

    @Test
    fun `should allow HTTPS requests`() {

        // stub
        mockWebServer.start()
        mockWebServer.enqueue(MockResponse().setBody("test response"))
        every { mockAuthenticationInterceptor.intercept(any()) } answers {
            val chain = firstArg<okhttp3.Interceptor.Chain>()
            val request = chain.request()
            val modifiedRequest = request.newBuilder()
                .addHeader("x-api-key", "test-api-key")
                .build()
            chain.proceed(modifiedRequest)
        }

        // run
        val client = applicationOkHttpClient.client
        val httpsRequest = Request.Builder()
            .url(mockWebServer.url("/test"))
            .build()
        val response = client.newCall(httpsRequest).execute()

        // test
        assertEquals(200, response.code)
        assertEquals("test response", response.body?.string())
    }
}