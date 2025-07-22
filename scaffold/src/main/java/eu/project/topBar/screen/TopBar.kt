package eu.project.topBar.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.project.topBar.model.TopBarViewState
import eu.project.ui.dimensions.DividerThickness
import eu.project.ui.dimensions.TopBarHeight
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.Divider

@Composable
internal fun topBar(
    topBarViewState: TopBarViewState
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = WidgetPadding.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                    .height(TopBarHeight.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                content = {

                    Text(stringResource(topBarViewState.screenName))
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