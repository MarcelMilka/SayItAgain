package eu.project.saved.exportWords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.ui.R
import eu.project.ui.components.banners.warningBanner
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun BoxScope.showReadyToExport(
    listState: LazyListState,
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            subscreenController(
                subscreenControllerState = uiState.subscreenControllerState,
                onClickLeft = { onClickLeft() },
                onClickRight = { onClickRight() }
            )

            if (uiState.showNoWordsSelectedBanner) {

                warningBanner(
                    headline = stringResource(R.string.no_words_selected),
                    body = stringResource(R.string.please_select_at_least_one_word_before_continuing),
                    testTag = TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = WidgetPadding.dp)
                    .testTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN),
                verticalArrangement = Arrangement.Top,
                content = {

                    this.items(uiState.exportableWords) { exportableWord ->

                        exportableWord.exportableSavedWordCard { onChangeWordSelection(exportableWord) }
                    }
                }
            )
        }
    )
}