package eu.project.connectivity

import android.content.Context
import eu.project.common.connectivity.ConnectivityObserver
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope

internal class ConnectivityObserverImpl @Inject constructor(
    private val applicationContext: Context,
    private val ioCoroutineScope: CoroutineScope
): ConnectivityObserver