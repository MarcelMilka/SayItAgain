package eu.project.localdata.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import eu.project.localdata.database.ApplicationDatabaseName
import eu.project.localdata.database.ApplicationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApplicationDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext applicationContext: Context): ApplicationDatabase =
        Room.databaseBuilder(
            context = applicationContext,
            klass = ApplicationDatabase::class.java,
            name = ApplicationDatabaseName
        ).build()
}