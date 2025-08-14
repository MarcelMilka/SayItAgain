package eu.project.common.testHelpers

import eu.project.common.model.SavedWord
import java.util.UUID

object SavedWordTestInstances {

    val list = listOf(
        SavedWord(UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"), "Cat", "English"),
        SavedWord(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "Monitor lizard", "English"),
        SavedWord(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "Hagetreboa", "Norwegian")
    )

    val first = list[0]
    val second = list[1]
    val third = list[2]
}