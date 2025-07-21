package eu.project.scaffold

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import eu.project.common.navigation.Navigation
import eu.project.home.impl.homeScreenImpl
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
        content = { paddingValues ->

            NavHost(
                modifier = Modifier.padding(paddingValues.calculateBottomPadding()),
                navController = controller,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                startDestination = Navigation.HomeScreen,
                builder = {

                    this.homeScreenImpl()
                }
            )
        }
    )
}