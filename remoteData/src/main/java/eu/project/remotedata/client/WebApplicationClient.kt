package eu.project.remotedata.client

import eu.project.remotedata.endpoint.ExportEndpoints

internal interface WebApplicationClient {

    val exportEndpoints: ExportEndpoints
}