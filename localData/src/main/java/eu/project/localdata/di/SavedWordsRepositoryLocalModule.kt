package eu.project.localdata.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.common.coroutineScope.IoDispatcherScope
import eu.project.common.localData.SavedWordsDataSource
import eu.project.common.localData.SavedWordsRepository
import eu.project.localdata.repository.SavedWordsRepositoryLocalImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SavedWordsRepositoryLocalModule {

    @Provides
    @Singleton
    fun bindSavedWordsRepository(
        @IoDispatcherScope ioCoroutineScope: CoroutineScope,
        savedWordsDataSource: SavedWordsDataSource
    ): SavedWordsRepository =
        SavedWordsRepositoryLocalImpl(
            ioCoroutineScope = ioCoroutineScope,
            savedWordsDataSource = savedWordsDataSource
        )
}