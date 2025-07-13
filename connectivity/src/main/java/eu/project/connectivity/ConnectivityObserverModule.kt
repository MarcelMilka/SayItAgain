package eu.project.connectivity

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.project.common.connectivity.ConnectivityObserver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ConnectivityObserverModule {

    @Provides
    @Singleton
    fun provideConnectivityObserver(
        @ApplicationContext applicationContext: Context
    ): ConnectivityObserver =
        ConnectivityObserverImpl(applicationContext)
}