package eu.project.localdata.converter

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.util.UUID

internal class UUIDConverterTest {

    private val converter = UUIDConverter()

    @Test
    fun `fromUUID should convert UUID to String`() {
        val uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        val expected = "550e8400-e29b-41d4-a716-446655440000"

        val actual = converter.fromUUID(uuid)

        assertEquals(expected, actual)
    }

    @Test
    fun `toUUID should convert String to UUID`() {
        val uuidString = "123e4567-e89b-12d3-a456-426614174000"
        val expected = UUID.fromString(uuidString)

        val actual = converter.toUUID(uuidString)

        assertEquals(expected, actual)
    }

    @Test
    fun `toUUID and fromUUID should be symmetrical`() {
        val original = UUID.randomUUID()
        val converted = converter.toUUID(converter.fromUUID(original))

        assertEquals(original, converted)
    }
}