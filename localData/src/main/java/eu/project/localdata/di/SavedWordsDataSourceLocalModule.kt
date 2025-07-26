package eu.project.localdata.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.common.localData.SavedWordsDataSource
import eu.project.localdata.datasource.SavedWordsDataSourceLocalImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SavedWordsDataSourceLocalModule {

    @Binds
    @Singleton
    abstract fun bindSavedWordsDataSource(impl: SavedWordsDataSourceLocalImpl):
            SavedWordsDataSource
}