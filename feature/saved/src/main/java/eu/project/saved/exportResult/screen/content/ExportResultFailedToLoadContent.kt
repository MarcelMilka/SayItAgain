package eu.project.saved.exportResult.screen.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.ui.R
import eu.project.ui.components.buttons.primaryButton
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.spacers.spacerVertical32
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineLarge
import eu.project.ui.dimensions.ThumbReachPadding
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun ColumnScope.exportResultFailedToLoadFileContent(
    throwable: Throwable,
    onClickTryAgainLater: () -> Unit
) {

    // upper content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Image(
                painter = painterResource(id = R.drawable.error),
                contentDescription = stringResource(R.string.illustration_error_description)
            )
        }
    )

    // bottom content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    spacerVertical32()

                    headlineLarge(text = stringResource(R.string.failed_to_load_data))

                    spacerV8()

                    bodyLarge(
                        text = "${throwable.message}",
                        color = SecondaryWhite,
                        textAlign = TextAlign.Center
                    )
                }
            )

            Column(
                modifier = Modifier
                    .padding(bottom = ThumbReachPadding.dp),
                content = {

                    primaryButton(
                        text = stringResource(R.string.try_again_later),
                        onClick = onClickTryAgainLater,
                        testTag = TestTags.EXPORT_RESULT_SCREEN_PRIMARY_BUTTON_TRY_AGAIN_LATER
                    )
                }
            )
        }
    )
}