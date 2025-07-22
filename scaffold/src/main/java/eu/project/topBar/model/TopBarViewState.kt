package eu.project.topBar.model

import eu.project.ui.R

data class TopBarViewState(
    val displayTopBar: Boolean = true,
    val showBackIcon: Boolean = false,
    val screenName: Int = R.string.home,
    val showInfoIcon: Boolean = true,
)