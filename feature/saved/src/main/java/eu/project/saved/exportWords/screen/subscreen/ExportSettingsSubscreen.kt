package eu.project.saved.exportWords.screen.subscreen

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.ui.emailTextField
import eu.project.saved.exportWords.ui.exportMethod
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV16
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.headlineSmall

@Composable
internal fun ColumnScope.exportSettingsSubscreen(
    uiState: ExportWordsUiState,
    onClickSendMethod: () -> Unit,
    onClickDownloadMethod: () -> Unit
) {

    // text field controllers
    var focusRequester = remember { FocusRequester() }
    var email by remember { mutableStateOf("") }
    var focused by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.showEmailTextField) {

        when(uiState.showEmailTextField) {
            true -> focusRequester.requestFocus()
            false -> {}
        }
    }


    // UI
    headlineSmall(stringResource(R.string.how_would_you_like_to_export_your_words))

    spacerV8()

    exportMethod(
        uiState.exportMethodPickerState.sendMethodState,
        onClick = { onClickSendMethod() }
    )

    spacerV8()

    if (uiState.showEmailTextField) {

        headlineSmall(stringResource(R.string.enter_the_email_to_receive_the_file))

        spacerV8()

        emailTextField(
            focusRequester = focusRequester,
            isPlaceholderVisible = email == "",
            value = email,
            onValueChange = { email = it },
            onFocusChange = { focused = it },
            isConfirmActive = email != "" && email.length > 3,
            onConfirmClick = {  }
        )

        spacerV16()
    }

    exportMethod(
        uiState.exportMethodPickerState.downloadMethodState,
        onClick = { onClickDownloadMethod() }
    )
}