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
import eu.project.ui.R
import eu.project.ui.components.banners.warningBanner
import eu.project.ui.components.buttons.primaryButton
import eu.project.ui.components.buttons.textButton
import eu.project.ui.components.spacers.spacerV16
import eu.project.ui.components.text.headlineLarge
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
            .testTag(TestTags.HOME_SCREEN),
        content = {

            // warning banner (top of the screen)
            Column(
                modifier = Modifier.align(Alignment.TopCenter),
                content = {

                    animatedVisibilityWrapper(
                        visible = !isNetworkAvailable,
                        content = {

                            warningBanner(
                                headline = stringResource(R.string.you_are_offline),
                                body = stringResource(R.string.you_are_offline_explanation),
                                testTag = TestTags.HOME_SCREEN_NO_CONNECTION_BANNER
                            )
                        }
                    )
                }
            )

            // CTA content
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {

                    headlineLarge(text = stringResource(R.string.struggling_to_catch_every_word))

                    spacerV16()

                    primaryButton(
                        text = stringResource(R.string.pick_and_transcribe),
                        onClick = onNavigateSelectAudioScreen,
                        testTag = TestTags.HOME_SCREEN_PRIMARY_BUTTON_PRICK_AND_TRANSCRIBE
                    )
                }
            )

            // Button 'My vocabulary'
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
    )
}

// TODO: replace with animatedVisibilityWrapper from the module ui
@Composable
private fun animatedVisibilityWrapper(
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