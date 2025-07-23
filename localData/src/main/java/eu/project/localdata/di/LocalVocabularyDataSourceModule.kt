package eu.project.localdata.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.common.localData.LocalVocabularyDataSource
import eu.project.localdata.datasource.LocalVocabularyDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalVocabularyDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalVocabularyDataSource(impl: LocalVocabularyDataSourceImpl):
            LocalVocabularyDataSource
}