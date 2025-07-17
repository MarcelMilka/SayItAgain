package eu.project.scaffold

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.project.common.navigation.Navigation
import eu.project.ui.dimensions.Padding
import eu.project.ui.theme.Background

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun applicationScaffold() {

    val controller = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Background,
        contentWindowInsets = WindowInsets.statusBars,
        content = {

            NavHost(
                modifier = Modifier.padding(horizontal = Padding.dp),
                navController = controller,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                startDestination = Navigation.HomeScreen,
                builder = {

                    composable<Navigation.HomeScreen> {}
                }
            )
        }
    )
}