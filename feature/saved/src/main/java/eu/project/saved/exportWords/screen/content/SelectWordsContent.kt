package eu.project.saved.exportWords.screen.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.saved.exportWords.model.ExportableSavedWord
import eu.project.saved.exportWords.state.SelectWordsUiState
import eu.project.saved.exportWords.ui.exportableSavedWordCard
import eu.project.ui.R
import eu.project.ui.components.banners.warningBanner
import eu.project.ui.dimensions.WidgetPadding

@Composable
internal fun ColumnScope.selectWordsContent(
    selectWordsUiState: SelectWordsUiState,
    wordsToExport: List<ExportableSavedWord>,
    onChangeWordSelection: (ExportableSavedWord) -> Unit
) {

    animatedVisibilityWrapper(
        visible = selectWordsUiState.showNoWordsSelectedBanner,
        content = {

            warningBanner(
                headline = stringResource(R.string.no_words_selected),
                body = stringResource(R.string.please_select_at_least_one_word_before_continuing),
                testTag = TestTags.EXPORT_WORDS_SCREEN_WARNING_BANNER
            )
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = WidgetPadding.dp)
            .testTag(TestTags.EXPORT_WORDS_SCREEN_LAZY_COLUMN),
        verticalArrangement = Arrangement.Top,
        content = {

            this.items(wordsToExport) { exportableWord ->

                exportableWord.exportableSavedWordCard { onChangeWordSelection(exportableWord) }
            }
        }
    )
}

// TODO: put it into the module 'ui'
@Composable
fun animatedVisibilityWrapper(
    visible: Boolean,
    content: @Composable() () -> Unit
) {

    // as the composable is going to be used globally without
    // any other options, I decided to hardcode the values

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { -40 },
            animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
        ),
        content = { content() }
    )
}