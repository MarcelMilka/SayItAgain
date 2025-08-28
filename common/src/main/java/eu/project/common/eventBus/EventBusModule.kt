package eu.project.common.eventBus

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import eu.project.common.eventBus.saveFile.SaveFileEvent
import eu.project.common.eventBus.saveFile.SaveFileEventBus
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EventBusModule {

    @Provides
    @Singleton
    @SaveFileEventBusQualifier
    fun provideSaveFileEventBus(): EventBus<SaveFileEvent> =
        SaveFileEventBus()
}