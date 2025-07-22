package eu.project.topBar

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import eu.project.ui.dimensions.DividerThickness
import eu.project.ui.dimensions.TopBarHeight
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.Divider

@Composable
fun topBar(controller: NavHostController) {

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
                content = {}
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = DividerThickness.dp,
                color = Divider
            )
        }
    )
}