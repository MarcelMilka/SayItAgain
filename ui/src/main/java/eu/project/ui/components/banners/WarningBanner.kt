package eu.project.ui.components.banners

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.project.ui.R
import eu.project.ui.components.spacers.spacerH8
import eu.project.ui.components.spacers.spacerV8
import eu.project.ui.components.text.bodyLarge
import eu.project.ui.components.text.headlineSmall
import eu.project.ui.dimensions.WidgetCornerRadius
import eu.project.ui.dimensions.WidgetPadding
import eu.project.ui.theme.PrimaryWhite
import eu.project.ui.theme.WarningBanner

@Composable
fun warningBanner(
    headline: String,
    body: String,
    testTag: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = WarningBanner,
                shape = RoundedCornerShape(WidgetCornerRadius.dp)
            )
            .padding(WidgetPadding.dp)
            .testTag(testTag),
        content = {

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                content = {

                    Icon(
                        imageVector = Icons.Rounded.Warning,
                        contentDescription = stringResource(R.string.warning_icon_description),
                        tint = PrimaryWhite
                    )

                    spacerH8()

                    headlineSmall(headline)
                }
            )

            spacerV8()

            bodyLarge(
                text = body,
                color = PrimaryWhite,
                textAlign = TextAlign.Start
            )
        }
    )
}