package eu.project.common.eventBus.saveFile

import eu.project.common.eventBus.EventBus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class SaveFileEventBus @Inject constructor(): EventBus<SaveFileEvent> {

    private val _events = MutableSharedFlow<SaveFileEvent>(replay = 1)
    override val events: SharedFlow<SaveFileEvent> = _events.asSharedFlow()

    override suspend fun emit(event: SaveFileEvent) {

        _events.emit(event)
    }
}