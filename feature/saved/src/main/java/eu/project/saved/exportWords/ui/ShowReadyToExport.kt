package eu.project.saved.exportWords.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportWordsSubscreen
import eu.project.saved.exportWords.model.ExportWordsUiState
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.screen.subscreen.exportSettingsSubscreen
import eu.project.saved.exportWords.screen.subscreen.selectWordsSubscreen

@Composable
internal fun BoxScope.showReadyToExport(
    listState: LazyListState,
    uiState: ExportWordsUiState,
    onChangeWordSelection: (ExportableSavedWord) -> Unit,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.EXPORT_WORDS_SCREEN_SHOW_READY_TO_EXPORT),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            subscreenController(
                subscreenControllerState = uiState.subscreenControllerState,
                onClickLeft = { onClickLeft() },
                onClickRight = { onClickRight() }
            )

            when(uiState.subscreenControllerState.exportWordsSubscreen) {

                ExportWordsSubscreen.SelectWords -> selectWordsSubscreen(
                    listState = listState,
                    uiState = uiState,
                    onChangeWordSelection = onChangeWordSelection
                )

                ExportWordsSubscreen.ExportSettings -> exportSettingsSubscreen()
            }
        }
    )
}