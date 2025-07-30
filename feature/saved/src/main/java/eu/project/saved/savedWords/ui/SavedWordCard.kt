package eu.project.saved.savedWords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.common.model.SavedWord
import eu.project.ui.R
import eu.project.ui.components.buttons.secondaryIconButton
import eu.project.ui.components.spacers.spacerV4
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.bodySmall
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun SavedWord.savedWordCard(onDelete: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = WidgetPadding.dp)
            .testTag("$this"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = {

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    bodyLarge(
                        text = this@savedWordCard.word,
                        color = PrimaryWhite,
                        textAlign = TextAlign.Start
                    )

                    spacerV4()

                    bodySmall(
                        text = this@savedWordCard.language,
                        color = SecondaryWhite,
                        textAlign = TextAlign.Start
                    )
                }
            )

            secondaryIconButton(
                onClick = { onDelete() },
                painter = painterResource(R.drawable.delete),
                contentDescription = "${stringResource(R.string.delete)} - ${this@savedWordCard}",
                testTag = TestTags.SAVED_WORDS_SCREEN_SAVED_WORD_CARD_DELETE_ICON
            )
        }
    )
}