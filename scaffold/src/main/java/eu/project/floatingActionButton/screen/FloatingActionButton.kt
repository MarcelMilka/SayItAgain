package eu.project.floatingActionButton.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import eu.project.common.navigation.Screens
import eu.project.floatingActionButton.model.FloatingActionButtonViewState
import eu.project.floatingActionButton.model.FloatingActionButtonVisibilityState
import eu.project.floatingActionButton.ui.floatingActionButtonContent
import eu.project.ui.dimensions.ThumbReachPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import eu.project.common.TestTags

@Composable
internal fun floatingActionButton(
    visibilityState:  FloatingActionButtonVisibilityState,
    viewState:  FloatingActionButtonViewState,
    currentScreen: String?,

    onNavigateExportWords: () -> Unit
) {

    AnimatedVisibility(
        visible = visibilityState is FloatingActionButtonVisibilityState.Visible,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { -40 },
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut() + slideOutVertically(
            targetOffsetY = { -40 },
            animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing)
        ),
        content = {

            FloatingActionButton(
                onClick = {

                    if (currentScreen == Screens.SAVED_WORDS) {

                        onNavigateExportWords()
                    }
                },
                modifier = Modifier
                    .padding(bottom = ThumbReachPadding.dp)
                    .testTag(TestTags.FLOATING_ACTION_BUTTON),
                shape = RoundedCornerShape(percent = 100),
                containerColor = viewState.containerColor,
                contentColor = viewState.contentColor,
                elevation = FloatingActionButtonDefaults.elevation(20.dp),
                content = {

                    floatingActionButtonContent(
                        viewState = viewState
                    )
                }
            )
        }
    )
}