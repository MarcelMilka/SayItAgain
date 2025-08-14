package eu.project.saved.exportWords.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import eu.project.ui.components.text.labelMedium
import eu.project.ui.dimensions.WidgetCornerRadius
import eu.project.ui.theme.Primary
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.SecondaryWhite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import eu.project.ui.components.spacers.spacerH8
import eu.project.ui.theme.Disabled
import eu.project.ui.theme.OnPrimary

@Composable
fun emailTextField(
    focusRequester: FocusRequester,
    isPlaceholderVisible: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    isConfirmActive: Boolean,
    onConfirmClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester),
        shape = RoundedCornerShape(WidgetCornerRadius.dp),
        color = Color.Transparent,
        border = BorderStroke(2.dp, PrimaryWhite),
        onClick = { focusRequester.requestFocus() },
        tonalElevation = 0.dp,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterStart,
                content = {

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        content = {

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.CenterStart,
                                content = {

                                    if (isPlaceholderVisible) {
                                        labelMedium("E-mail", SecondaryWhite)
                                    }

                                    BasicTextField(
                                        value = value,
                                        onValueChange = { onValueChange(it) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .onFocusChanged {
                                                onFocusChange(it.isFocused)
                                            },
                                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = PrimaryWhite),
                                        cursorBrush = SolidColor(PrimaryWhite),
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                keyboardController?.hide()
                                                onFocusChange(false)
                                            }
                                        ),
                                        decorationBox = { innerTextField -> innerTextField() }
                                    )
                                }
                            )

                            spacerH8()

                            IconButton(
                                onClick = {
                                    keyboardController?.hide()
                                    onFocusChange(false)
                                    onConfirmClick()
                                },
                                enabled = isConfirmActive,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f) // ensures square
                                    .background(
                                        color = if (isConfirmActive) Primary else Disabled,
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                content = {

                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Confirm",
                                        tint = if (isConfirmActive) OnPrimary else SecondaryWhite
                                    )
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}