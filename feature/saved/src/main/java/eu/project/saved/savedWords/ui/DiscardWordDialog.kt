package eu.project.saved.savedWords.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import eu.project.common.TestTags
import eu.project.common.model.SavedWord
import eu.project.ui.R
import eu.project.ui.components.buttons.primaryButton
import eu.project.ui.components.buttons.secondaryButton
import eu.project.ui.components.spacers.spacerH8
import eu.project.ui.components.spacers.spacerV16
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineMedium
import eu.project.ui.dimensions.ScreenPadding
import eu.project.ui.dimensions.WidgetCornerRadius
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.Background
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun discardWordDialog(
    wordToDelete: SavedWord,
    onDelete: (SavedWord) -> Unit,
    onCancel: () -> Unit
) {

    Dialog(
        onDismissRequest = { onCancel() },
        content = {

            Box(
                modifier = Modifier
                    .background(
                        color = Background,
                        shape = RoundedCornerShape(WidgetCornerRadius.dp)
                    )
                    .padding(ScreenPadding.dp)
                    .testTag(TestTags.DISCARD_WORD_DIALOG),
                content = {

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        content = {

                            headlineMedium(text = stringResource(R.string.discard_the_word))

                            spacerV8()

                            bodyLarge(
                                text = "${stringResource(R.string.the_word)} '${wordToDelete.word}' ${stringResource(R.string.will_be_deleted_forever)}",
                                color = SecondaryWhite,
                                textAlign = TextAlign.Start
                            )

                            spacerV16()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = WidgetPadding.dp),
                                horizontalArrangement = Arrangement.End,
                                content = {

                                    secondaryButton(
                                        text = stringResource(R.string.delete),
                                        onClick = { onDelete(wordToDelete) },
                                        testTag = TestTags.SAVED_WORDS_SCREEN_SECONDARY_BUTTON
                                    )

                                    spacerH8()

                                    primaryButton(
                                        text = stringResource(R.string.cancel),
                                        onClick = { onCancel() },
                                        testTag = TestTags.SAVED_WORDS_SCREEN_PRIMARY_BUTTON
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