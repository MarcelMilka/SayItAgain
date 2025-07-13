package eu.project.common.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineScopeModule {

    @Provides
    @Singleton
    @IoCoroutineScope
    fun provideIoScope(): CoroutineScope =
        // if one coroutine fails, do not cancel its siblings
        CoroutineScope(SupervisorJob() + Dispatchers.IO)
}