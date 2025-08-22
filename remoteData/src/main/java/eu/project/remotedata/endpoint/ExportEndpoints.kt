package eu.project.remotedata.endpoint

import eu.project.remotedata.dto.DownloadExportRequestDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

internal interface ExportEndpoints {

    @POST("/siaapi/exports/download")
    suspend fun downloadExport(@Body downloadExportRequestDto: DownloadExportRequestDto): Response<ResponseBody>
}