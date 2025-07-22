package eu.project.topBar.model

import eu.project.ui.R

data class TopBarViewState(
    val displayTopBar: Boolean = false,

    val showBackIcon: Boolean = false,

    val showScreenName: Boolean = false,
    val screenName: Int = R.string.home,

    val showInfoIcon: Boolean = true,
)