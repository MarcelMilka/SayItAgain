package eu.project.saved.exportWords.model

import androidx.compose.ui.graphics.Color.Companion.Transparent
import eu.project.ui.R
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.SecondaryWhite

enum class ExportMethod {
    SendToEmail,
    DownloadToDevice,
    NotSpecified
}

object ExportMethodVariants {

    val sendNotSelected = ExportMethodState(
        label = R.string.send_to_email,
        body = R.string.send_to_email_explanation,
        enabled = true,
        labelColor = PrimaryWhite,
        bodyColor = SecondaryWhite,
        backgroundColor = Transparent,
        borderColor = SecondaryWhite
    )

    val sendSelected = ExportMethodState(
        label = R.string.send_to_email,
        body = R.string.send_to_email_explanation,
        enabled = false,
        labelColor = OnPrimary,
        bodyColor = OnPrimary,
        backgroundColor = Primary,
        borderColor = Primary
    )

    val downloadNotSelected = ExportMethodState(
        label = R.string.download_to_device,
        body = R.string.download_to_device_explanation,
        enabled = true,
        labelColor = PrimaryWhite,
        bodyColor = SecondaryWhite,
        backgroundColor = Transparent,
        borderColor = SecondaryWhite
    )

    val downloadSelected = ExportMethodState(
        label = R.string.download_to_device,
        body = R.string.download_to_device_explanation,
        enabled = false,
        labelColor = OnPrimary,
        bodyColor = OnPrimary,
        backgroundColor = Primary,
        borderColor = Primary
    )
}