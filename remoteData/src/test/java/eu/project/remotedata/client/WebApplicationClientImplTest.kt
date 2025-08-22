package eu.project.remotedata.client

import eu.project.remotedata.BuildConfig
import eu.project.remotedata.endpoint.ExportEndpoints
import eu.project.remotedata.interceptor.AuthenticationInterceptor
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class WebApplicationClientImplTest {

    private lateinit var mockAuthenticationInterceptor: AuthenticationInterceptor
    private lateinit var applicationOkHttpClient: ApplicationOkHttpClient
    private lateinit var webApplicationClient: WebApplicationClientImpl
    private lateinit var mockExportEndpoints: ExportEndpoints

    @Before
    fun setUp() {
        mockAuthenticationInterceptor = mockk()
        applicationOkHttpClient = ApplicationOkHttpClient(mockAuthenticationInterceptor)
        webApplicationClient = WebApplicationClientImpl(applicationOkHttpClient)
        mockExportEndpoints = mockk()
    }

    @After
    fun tearDown() {
        // Cleanup if needed
    }

    @Test
    fun `should create WebApplicationClientImpl successfully`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = webApplicationClient

        // test
        assertNotNull(client)
        assertTrue(client is WebApplicationClientImpl)
    }

    @Test
    fun `should have correct base URL configuration`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = webApplicationClient

        // test
        assertEquals(BuildConfig.SIAAPI_BASE_URL, client.javaClass.getDeclaredField("baseUrl").apply { isAccessible = true }.get(client))
    }

    @Test
    fun `should create ExportEndpoints successfully`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val exportEndpoints = webApplicationClient.exportEndpoints

        // test
        assertNotNull(exportEndpoints)
        assertTrue(exportEndpoints is ExportEndpoints)
    }

    @Test
    fun `should use ApplicationOkHttpClient in Retrofit configuration`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val retrofit = webApplicationClient.javaClass.getDeclaredField("retrofit").apply { isAccessible = true }.get(webApplicationClient) as Retrofit

        // test
        assertNotNull(retrofit.callFactory())
    }

    @Test
    fun `should create ExportEndpoints with correct interface`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val exportEndpoints = webApplicationClient.exportEndpoints

        // test
        assertTrue(exportEndpoints.javaClass.interfaces.contains(ExportEndpoints::class.java))
    }

    @Test
    fun `should handle ExportEndpoints creation with proper Retrofit configuration`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val exportEndpoints = webApplicationClient.exportEndpoints

        // test
        assertNotNull(exportEndpoints)
    }

    @Test
    fun `should maintain singleton behavior for ExportEndpoints`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val exportEndpoints1 = webApplicationClient.exportEndpoints
        val exportEndpoints2 = webApplicationClient.exportEndpoints

        // test
        // Should return the same instance (singleton behavior)
        assertTrue(exportEndpoints1 === exportEndpoints2)
    }

    @Test
    fun `should configure Retrofit with proper timeout settings from ApplicationOkHttpClient`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val retrofit = webApplicationClient.javaClass.getDeclaredField("retrofit").apply { isAccessible = true }.get(webApplicationClient) as Retrofit
        val okHttpClient = applicationOkHttpClient.client

        // test
        // Verify that the Retrofit instance uses the same OkHttpClient with proper timeouts
        assertEquals(okHttpClient.connectTimeoutMillis, (retrofit.callFactory() as okhttp3.OkHttpClient).connectTimeoutMillis)
        assertEquals(okHttpClient.readTimeoutMillis, (retrofit.callFactory() as okhttp3.OkHttpClient).readTimeoutMillis)
    }

    @Test
    fun `should implement WebApplicationClient interface`() {

        // stub
        every { mockAuthenticationInterceptor.intercept(any()) } returns mockk()

        // run
        val client = webApplicationClient

        // test
        assertTrue(client is WebApplicationClient)
    }
}