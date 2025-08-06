package eu.project.saved.exportWords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun BoxScope.showReadyToExport(
    listState: LazyListState,
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit
) {

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WidgetPadding.dp)
            .align(Alignment.TopStart)
            .testTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN),
        verticalArrangement = Arrangement.Top,
        content = {

            this.items(uiState.exportableWords) { exportableWord ->

                exportableWord.exportableSavedWordCard { onChangeWordSelection(exportableWord) }
            }
        }
    )
}