package eu.project.remotedata.repository

import eu.project.common.model.SavedWord
import eu.project.common.remoteData.ExportError
import eu.project.common.remoteData.ExportRepository
import eu.project.remotedata.endpoint.ExportEndpoints
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ExportRepositoryImplTest {

    private lateinit var exportEndpoints: ExportEndpoints
    private lateinit var exportRepositoryImpl: ExportRepository

    @Before
    fun setUp() {

        exportEndpoints = mockk(relaxed = true)
        exportRepositoryImpl = ExportRepositoryImpl(exportEndpoints)
    }



    @Test
    fun `requestDownloadToDevice returns CsvFile when response is successful and body is not null`() = runTest {

        val json = """[{"Term":"Katt","Definiton":""},{"Term":"Hund","Definiton":""},{"Term":"Fugl","Definiton":""}]"""

        val responseBody = json.toByteArray().toResponseBody()
        val response = Response.success(responseBody)

        // stub
        coEvery { exportEndpoints.downloadExport(any()) } returns response

        // call
        val data = listOf<SavedWord>(
            SavedWord(UUID.randomUUID(), "Katt", "Norwegian"),
            SavedWord(UUID.randomUUID(), "Hund", "Norwegian"),
            SavedWord(UUID.randomUUID(), "Fugl", "Norwegian")
        )
        val csv = exportRepositoryImpl.requestDownloadToDevice(data)

        // test
        assertTrue(csv.isSuccess)
        assertEquals(json, csv.getOrNull()?.data?.decodeToString())

        // verify
        coVerify(exactly = 1) { exportEndpoints.downloadExport(any()) }
    }

    @Test
    fun `requestDownloadToDevice returns UnknownError when response is successful and body is null`() = runTest {

        val response = Response.success<ResponseBody>(null)

        // stub
        coEvery { exportEndpoints.downloadExport(any()) } returns response

        // call
        val data = listOf<SavedWord>(
            SavedWord(UUID.randomUUID(), "Katt", "Norwegian"),
            SavedWord(UUID.randomUUID(), "Hund", "Norwegian")
        )
        val result = exportRepositoryImpl.requestDownloadToDevice(data)

        // test
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertTrue(error is ExportError.UnknownError)
        assertEquals("Empty response body", error?.message)

        // verify
        coVerify(exactly = 1) { exportEndpoints.downloadExport(any()) }
    }

    @Test
    fun `requestDownloadToDevice returns UnknownError when an exception occurs`() = runTest {

        val exceptionMessage = "Network connection failed"

        // stub
        coEvery { exportEndpoints.downloadExport(any()) } throws RuntimeException(exceptionMessage)

        // call
        val data = listOf<SavedWord>(
            SavedWord(UUID.randomUUID(), "Katt", "Norwegian")
        )
        val result = exportRepositoryImpl.requestDownloadToDevice(data)

        // test
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertTrue(error is ExportError.UnknownError)
        assertEquals(exceptionMessage, error?.message)

        // verify
        coVerify(exactly = 1) { exportEndpoints.downloadExport(any()) }
    }

    @Test
    fun `requestDownloadToDevice returns UnexpectedErrorOccurred when an exception occurs`() = runTest {

        val exceptionMessage = "Unexpected error occurred"

        // stub
        coEvery { exportEndpoints.downloadExport(any()) } throws RuntimeException()

        // call
        val data = listOf<SavedWord>(
            SavedWord(UUID.randomUUID(), "Katt", "Norwegian")
        )
        val result = exportRepositoryImpl.requestDownloadToDevice(data)

        // test
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()
        assertTrue(error is ExportError.UnknownError)
        assertEquals(exceptionMessage, error?.message)

        // verify
        coVerify(exactly = 1) { exportEndpoints.downloadExport(any()) }
    }
}