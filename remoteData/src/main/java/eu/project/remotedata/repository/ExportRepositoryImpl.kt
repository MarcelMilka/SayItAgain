package eu.project.remotedata.repository

import com.google.gson.Gson
import eu.project.common.model.SavedWord
import eu.project.common.remoteData.CsvFile
import eu.project.common.remoteData.ExportError
import eu.project.common.remoteData.ExportRepository
import eu.project.remotedata.dto.convertToDto
import eu.project.remotedata.endpoint.ExportEndpoints
import javax.inject.Inject

internal class ExportRepositoryImpl @Inject constructor(
    private val exportEndpoints: ExportEndpoints
): ExportRepository {

    private val gson = Gson()


    override suspend fun requestDownloadToDevice(wordsToExport: List<SavedWord>): Result<CsvFile> {

        return try {

            val response = exportEndpoints.downloadExport(wordsToExport.convertToDto())

            when(response.isSuccessful) {
                true -> {

                    val csvBytes = response.body()?.bytes()
                        ?: return Result.failure(ExportError.UnknownError("Empty response body"))

                    Result.success(CsvFile(data = csvBytes))
                }
                false -> {

                    val validationErrors = retrieveValidationErrors(response.errorBody()?.string())
                    Result.failure(ExportError.ValidationError(validationErrors))
                }
            }
        }

        catch (e: Exception) {

            Result.failure(ExportError.UnknownError(e.message ?: "Unexpected error occurred"))
        }
    }

    private fun retrieveValidationErrors(errorBody: String?): List<String> {

        return try {

            errorBody?.let { json ->

                gson.fromJson(json, Array<String>::class.java).toList()
            } ?: listOf("Unknown validation error")
        }

        catch (e: Exception) {

            listOf(e.message ?: "Failed to retrieve validation errors")
        }
    }
}