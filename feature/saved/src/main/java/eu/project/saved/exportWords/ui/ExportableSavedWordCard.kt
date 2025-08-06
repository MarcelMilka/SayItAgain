package eu.project.saved.exportWords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.ui.components.checkboxes.checkbox
import eu.project.ui.components.spacers.spacerH8
import eu.project.ui.components.spacers.spacerV4
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.bodySmall
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.SecondaryWhite

@Composable
internal fun ExportableSavedWord.exportableSavedWordCard(
    onClick: (ExportableSavedWord) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = WidgetPadding.dp)
            .testTag("${this.uuid}"),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        content = {

            checkbox(
                checked = this@exportableSavedWordCard.toExport,
                testTag = this@exportableSavedWordCard.word,
                onCheckedChange = { onClick(this@exportableSavedWordCard) }
            )

            spacerH8()

            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = {

                    bodyLarge(
                        text = this@exportableSavedWordCard.word,
                        color = PrimaryWhite,
                        textAlign = TextAlign.Start
                    )

                    spacerV4()

                    bodySmall(
                        text = this@exportableSavedWordCard.language,
                        color = SecondaryWhite,
                        textAlign = TextAlign.Start
                    )
                }
            )
        }
    )
}