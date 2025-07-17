package eu.project.scaffold

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.project.ui.theme.Background

@Composable
fun applicationScaffold() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Background,
        content = {}
    )
}