package eu.project.connectivity

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.project.common.connectivity.ConnectivityObserver
import eu.project.common.coroutine.IoCoroutineScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ConnectivityObserverModule {

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext applicationContext: Context,
        @IoCoroutineScope ioCoroutineScope: CoroutineScope
    ): ConnectivityObserver {

        return ConnectivityObserverImpl(
            applicationContext = applicationContext,
            ioCoroutineScope = ioCoroutineScope
        )
    }
}