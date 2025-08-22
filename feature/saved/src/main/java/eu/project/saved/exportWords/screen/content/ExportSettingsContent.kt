package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.project.common.TestTags
import eu.project.saved.exportWords.state.ExportSettingsUiState
import eu.project.saved.exportWords.ui.exportMethod
import eu.project.ui.R
import eu.project.ui.components.banners.warningBanner
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.headlineSmall

@Composable
internal fun ColumnScope.exportSettingsContent(
    exportSettingsUiState: ExportSettingsUiState,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    animatedVisibilityWrapper(
        visible = exportSettingsUiState.showExportMethodNotAvailableBanner,
        content = {

            warningBanner(
                headline = stringResource(R.string.export_method_not_available),
                body = stringResource(R.string.export_method_not_available_explanation),
                testTag = TestTags.EXPORT_WORDS_SCREEN_EXPORT_SETTINGS_WARNING_BANNER
            )
        }
    )

    headlineSmall(stringResource(R.string.how_would_you_like_to_export_your_words))

    spacerV8()

    exportMethod(
        state = exportSettingsUiState.sendMethodState,
        onClick = onClickSendMethod
    )

    spacerV8()

    exportMethod(
        state = exportSettingsUiState.downloadMethodState,
        onClick = onClickDownloadMethod
    )
}