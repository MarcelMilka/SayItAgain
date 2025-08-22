package eu.project.remotedata.dto

import eu.project.common.model.SavedWord
import eu.project.common.testHelpers.SavedWordTestInstances
import junit.framework.TestCase.assertEquals
import org.junit.Test

class DownloadExportRequestDtoTest {

    // 'ti' is an abbreviation for TestInstances
    private val ti = SavedWordTestInstances
    private val savedWords = ti.list

    @Test
    fun `convertToDto should map SavedWord list to DownloadExportRequestDto`() {

        val expected = DownloadExportRequestDto(
            wordsToExport = listOf(
                WordDto(value = ti.first.word),
                WordDto(value = ti.second.word),
                WordDto(value = ti.third.word)
            )
        )

        val actual = savedWords.convertToDto()

        assertEquals(savedWords.count(), actual.wordsToExport.count())
        assertEquals(expected, actual)
    }

    @Test
    fun `convertToDto should handle empty list`() {

        val savedWords = emptyList<SavedWord>()

        val result = savedWords.convertToDto()

        assertEquals(0, result.wordsToExport.size)
    }

    @Test
    fun `convertToDto should only use word field from SavedWord`() {

        val result = listOf(ti.first).convertToDto()

        assertEquals(ti.first.word, result.wordsToExport[0].value)
    }
}