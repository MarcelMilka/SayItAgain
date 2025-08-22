package eu.project.remotedata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.common.remoteData.ExportRepository
import eu.project.remotedata.client.WebApplicationClient
import eu.project.remotedata.repository.ExportRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ExportRepositoryModule {

    @Provides
    @Singleton
    fun provideSavedWordDAO(client: WebApplicationClient): ExportRepository =
        ExportRepositoryImpl(exportEndpoints = client.exportEndpoints)
}