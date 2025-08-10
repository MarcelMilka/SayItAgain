package eu.project.saved.exportWords.screen.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import eu.project.saved.exportWords.state.ExportWordsUiState
import eu.project.saved.exportWords.ui.emailTextField
import eu.project.saved.exportWords.ui.exportMethod
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerV16
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.headlineSmall
import eu.project.ui.components.text.labelMedium
import eu.project.ui.theme.PrimaryWhite

@Composable
internal fun ColumnScope.exportSettingsContent(
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
        state = uiState.exportMethodControllerState.sendMethodState,
        onClick = onClickSendMethod
    )

    spacerV8()

    if (uiState.showEmailTextField) {

        spacerV8()

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            content = {

                labelMedium(
                    text = stringResource(R.string.or),
                    color = PrimaryWhite
                )
            }
        )

        spacerV16()
    }

    exportMethod(
        state = uiState.exportMethodControllerState.downloadMethodState,
        onClick = onClickDownloadMethod
    )
}