package eu.project.localdata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.localdata.dao.SavedWordDAO
import eu.project.localdata.database.ApplicationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SavedWordDaoModule {

    @Provides
    @Singleton
    fun provideDatabase(database: ApplicationDatabase): SavedWordDAO =
        database.savedWordDAO()
}