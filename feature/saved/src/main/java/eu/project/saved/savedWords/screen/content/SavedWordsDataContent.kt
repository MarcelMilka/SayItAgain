package eu.project.saved.savedWords.screen.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.ui.savedWordCard
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun ColumnScope.savedWordsDataContent(
    retrievedData: List<SavedWord>,
    onRequestDelete: (SavedWord) -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WidgetPadding.dp)
            .testTag(tag = TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            this.items(retrievedData) { savedWord ->

                savedWord.savedWordCard { onRequestDelete(savedWord) }
            }
        }
    )
}