package eu.project.common.model

import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ExportSettingsTest {



//- encodeToString tests ------------------------------------------------------------------------------------------------

    @Test
    fun `encodeToString should serialize ExportSettings to JSON string`() {

        // Given
        val words = listOf(
            Word("hello"),
            Word("world"),
            Word("test")
        )
        val exportSettings = ExportSettings(words)

        // When
        val result = exportSettings.encodeToString()

        // Then
        assertNotNull(result)
        assertEquals(
            """{"wordsToExport":[{"value":"hello"},{"value":"world"},{"value":"test"}]}""",
            result
        )
    }

    @Test
    fun `encodeToString should handle empty word list`() {

        // Given
        val exportSettings = ExportSettings(emptyList())

        // When
        val result = exportSettings.encodeToString()

        // Then
        assertNotNull(result)
        assertEquals("""{"wordsToExport":[]}""", result)
    }

    @Test
    fun `encodeToString should handle single word`() {

        // Given
        val exportSettings = ExportSettings(listOf(Word("single")))

        // When
        val result = exportSettings.encodeToString()

        // Then
        assertNotNull(result)
        assertEquals("""{"wordsToExport":[{"value":"single"}]}""", result)
    }

    @Test
    fun `encodeToString should handle special characters in word values`() {

        // Given
        val words = listOf(
            Word("hello world"),
            Word("test@email.com"),
            Word("word with \"quotes\""),
            Word("word with 'apostrophe'"),
            Word("word with \n newline"),
            Word("word with \t tab")
        )
        val exportSettings = ExportSettings(words)

        // When
        val result = exportSettings.encodeToString()

        // Then
        assertNotNull(result)
        // The JSON should be properly escaped
        assert(result.contains("hello world"))
        assert(result.contains("test@email.com"))
    }



//- decodeToExportSettings tests ----------------------------------------------------------------------------------------

    @Test
    fun `decodeToExportSettings should deserialize JSON string to ExportSettings`() {

        // Given
        val jsonString = """{"wordsToExport":[{"value":"hello"},{"value":"world"},{"value":"test"}]}"""

        // When
        val result = jsonString.decodeToExportSettings()

        // Then
        assertNotNull(result)
        assertEquals(3, result.wordsToExport.size)
        assertEquals("hello", result.wordsToExport[0].value)
        assertEquals("world", result.wordsToExport[1].value)
        assertEquals("test", result.wordsToExport[2].value)
    }

    @Test
    fun `decodeToExportSettings should handle empty word list`() {

        // Given
        val jsonString = """{"wordsToExport":[]}"""

        // When
        val result = jsonString.decodeToExportSettings()

        // Then
        assertNotNull(result)
        assertEquals(0, result.wordsToExport.size)
    }

    @Test
    fun `decodeToExportSettings should handle single word`() {

        // Given
        val jsonString = """{"wordsToExport":[{"value":"single"}]}"""

        // When
        val result = jsonString.decodeToExportSettings()

        // Then
        assertNotNull(result)
        assertEquals(1, result.wordsToExport.size)
        assertEquals("single", result.wordsToExport[0].value)
    }

    @Test
    fun `decodeToExportSettings should handle special characters in word values`() {

        // Given
        val jsonString = """{"wordsToExport":[{"value":"hello world"},{"value":"test@email.com"},{"value":"word with \"quotes\""}]}"""

        // When
        val result = jsonString.decodeToExportSettings()

        // Then
        assertNotNull(result)
        assertEquals(3, result.wordsToExport.size)
        assertEquals("hello world", result.wordsToExport[0].value)
        assertEquals("test@email.com", result.wordsToExport[1].value)
        assertEquals("word with \"quotes\"", result.wordsToExport[2].value)
    }

    @Test(expected = SerializationException::class)
    fun `decodeToExportSettings should throw SerializationException for invalid JSON`() {

        // Given
        val invalidJsonString = """{"wordsToExport":[{"value":"hello"},{"value":"world"}]"""

        // When & Then
        invalidJsonString.decodeToExportSettings()
    }

    @Test(expected = SerializationException::class)
    fun `decodeToExportSettings should throw SerializationException for malformed JSON`() {

        // Given
        val malformedJsonString = """{"wordsToExport":[{"value":"hello"},{"value":"world"}"""

        // When & Then
        malformedJsonString.decodeToExportSettings()
    }

    @Test(expected = SerializationException::class)
    fun `decodeToExportSettings should throw SerializationException for empty string`() {

        // Given
        val emptyString = ""

        // When & Then
        emptyString.decodeToExportSettings()
    }



//- integration tests ---------------------------------------------------------------------------------------------------

    @Test
    fun `encodeToString and decodeToExportSettings should be reversible`() {

        // Given
        val originalWords = listOf(
            Word("hello"),
            Word("world"),
            Word("test"),
            Word("with special chars: @#$%^&*()"),
            Word("with spaces and punctuation!")
        )
        val originalExportSettings = ExportSettings(originalWords)

        // When
        val encodedString = originalExportSettings.encodeToString()
        val decodedExportSettings = encodedString.decodeToExportSettings()

        // Then
        assertEquals(originalExportSettings.wordsToExport.size, decodedExportSettings.wordsToExport.size)
        originalExportSettings.wordsToExport.forEachIndexed { index, originalWord ->
            assertEquals(originalWord.value, decodedExportSettings.wordsToExport[index].value)
        }
    }

    @Test
    fun `encodeToString and decodeToExportSettings should handle unicode characters`() {

        // Given
        val words = listOf(
            Word("café"),
            Word("naïve"),
            Word("résumé"),
            Word("über"),
            Word("世界"),
            Word("こんにちは"),
            Word("안녕하세요")
        )
        val exportSettings = ExportSettings(words)

        // When
        val encodedString = exportSettings.encodeToString()
        val decodedExportSettings = encodedString.decodeToExportSettings()

        // Then
        assertEquals(words.size, decodedExportSettings.wordsToExport.size)
        words.forEachIndexed { index, originalWord ->
            assertEquals(originalWord.value, decodedExportSettings.wordsToExport[index].value)
        }
    }



//----------------------------------------------------------------------------------------------------------------------
}