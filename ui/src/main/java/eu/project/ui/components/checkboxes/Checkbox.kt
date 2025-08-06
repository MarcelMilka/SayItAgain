package eu.project.ui.components.checkboxes

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.testTag
import eu.project.ui.theme.OnPrimary
import eu.project.ui.theme.Primary
import eu.project.ui.theme.SecondaryWhite

@Composable
fun checkbox(
    checked: Boolean,
    testTag: String,
    onCheckedChange: () -> Unit
) {

    Checkbox(
        modifier = Modifier.testTag(testTag),
        checked = checked,
        onCheckedChange = { onCheckedChange() },
        colors = CheckboxColors(
            checkedCheckmarkColor = OnPrimary,
            checkedBoxColor = Primary,
            checkedBorderColor = Primary,

            uncheckedCheckmarkColor = Transparent,
            uncheckedBoxColor = Transparent,
            uncheckedBorderColor = SecondaryWhite,

            disabledCheckedBoxColor = Transparent,
            disabledUncheckedBoxColor = Transparent,
            disabledIndeterminateBoxColor = Transparent,
            disabledBorderColor = Transparent,
            disabledUncheckedBorderColor = Transparent,
            disabledIndeterminateBorderColor = Transparent
        ),
    )
}