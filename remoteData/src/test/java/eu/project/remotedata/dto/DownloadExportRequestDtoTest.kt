package eu.project.remotedata.dto

import eu.project.common.model.Word
import eu.project.common.testHelpers.WordTestInstances
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DownloadExportRequestDtoTest {

    // 'ti' is an abbreviation for TestInstances
    private val ti = WordTestInstances
    private val savedWords = ti.list

    @Test
    fun `convertToDto should map Word list to DownloadExportRequestDto`() {

        val expected = DownloadExportRequestDto(
            wordsToExport = listOf(
                WordDto(value = ti.first.value),
                WordDto(value = ti.second.value),
                WordDto(value = ti.third.value)
            )
        )

        val actual = savedWords.convertToDto()

        assertEquals(savedWords.count(), actual.wordsToExport.count())
        assertEquals(expected, actual)
    }

    @Test
    fun `convertToDto should handle empty list`() {

        val savedWords = emptyList<Word>()

        val result = savedWords.convertToDto()

        assertEquals(0, result.wordsToExport.size)
    }
}