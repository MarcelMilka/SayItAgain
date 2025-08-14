package eu.project.home.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.home.ui.noConnectionBanner
import eu.project.home.ui.pickAndTranscribeSection
import eu.project.ui.R
import eu.project.ui.components.buttons.textButton
import eu.project.ui.dimensions.ScreenPadding

@Composable
internal fun homeScreen(
    isNetworkAvailable: Boolean,
    onNavigateSelectAudioScreen: () -> Unit,
    onNavigateSavedWordsScreen: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = ScreenPadding.dp)
            .testTag(TestTags.HOME_SCREEN)
    ) {

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            content = {

                AnimatedVisibility(
                    visible = !isNetworkAvailable,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                    ),
                    exit = fadeOut() + slideOutVertically(
                        targetOffsetY = { -40 },
                        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
                    ),
                    content = {

                        // TODO: replace with  warningBanner
                        noConnectionBanner()
                    }
                )
            }
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = { pickAndTranscribeSection(onClick = onNavigateSelectAudioScreen) }
        )

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            content = {

                textButton(
                    text = stringResource(R.string.my_vocabulary),
                    onClick = onNavigateSavedWordsScreen,
                    testTag = TestTags.HOME_SCREEN_TEXT_BUTTON_MY_VOCABULARY
                )
            }
        )
    }
}