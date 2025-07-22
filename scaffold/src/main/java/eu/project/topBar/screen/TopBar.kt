package eu.project.topBar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.common.TestTags
import eu.project.topBar.model.TopBarViewState
import eu.project.ui.R
import eu.project.ui.components.buttons.iconButton
import eu.project.ui.components.spacers.spacerH8
import eu.project.ui.components.text.headlineMedium
import eu.project.ui.dimensions.DividerThickness
import eu.project.ui.dimensions.TopBarHeight
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.Divider

@Composable
internal fun topBar(
    topBarViewState: TopBarViewState,
    onNavigateBack: () -> Unit,
    onDisplayInfo: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = WidgetPadding.dp)
            .testTag(TestTags.TOP_BAR),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .height(TopBarHeight.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = {

                    if (topBarViewState.showBackIcon) {

                        iconButton(
                            onClick = { onNavigateBack() },
                            painter = painterResource(R.drawable.back),
                            contentDescription = stringResource(R.string.back_icon_description),
                            testTag = TestTags.TOP_BAR_ICON_BUTTON_BACK_ICON
                        )

                        spacerH8()

                        headlineMedium(text = stringResource(topBarViewState.screenName))
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (topBarViewState.showInfoIcon) {

                        iconButton(
                            onClick = { onDisplayInfo() },
                            painter = painterResource(R.drawable.info),
                            contentDescription = stringResource(R.string.info_icon_description),
                            testTag = TestTags.TOP_BAR_ICON_BUTTON_INFO_ICON
                        )
                    }
                }
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = DividerThickness.dp,
                color = Divider
            )
        }
    )
}