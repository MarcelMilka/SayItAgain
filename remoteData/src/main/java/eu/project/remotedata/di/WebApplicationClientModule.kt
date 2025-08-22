package eu.project.remotedata.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.remotedata.client.WebApplicationClient
import eu.project.remotedata.client.WebApplicationClientImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class WebApplicationClientModule {

    @Binds
    @Singleton
    abstract fun bindWebApplicationClient(impl: WebApplicationClientImpl):
            WebApplicationClient
}