package eu.project.common.eventBus

import kotlinx.coroutines.flow.SharedFlow

interface EventBus<Event> {

    val events: SharedFlow<Event>

    suspend fun emit(event: Event)
}