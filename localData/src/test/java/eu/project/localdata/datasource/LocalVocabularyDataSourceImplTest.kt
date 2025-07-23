package eu.project.localdata.datasource

import eu.project.localdata.dao.SavedWordDAO
import eu.project.localdata.entity.SavedWordEntity
import eu.project.localdata.entity.convertToModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class LocalVocabularyDataSourceImplTest {

    private lateinit var localVocabularyDataSourceImpl: LocalVocabularyDataSourceImpl
    private lateinit var savedWordDAO: SavedWordDAO

    private val firstEntityInstance = SavedWordEntity(
        uuid = UUID.fromString("a81bc81b-dead-4e5d-abff-90865d1e13b1"),
        word = "Cat",
        language = "English"
    )

    private val secondEntityInstance = SavedWordEntity(
        uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
        word = "Monitor lizard",
        language = "English"
    )

    private val thirdEntityInstance = SavedWordEntity(
        uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        word = "Hagetreboa",
        language = "Norwegian"
    )

    @Before
    fun setUp() {

        savedWordDAO = mockk(relaxed = true)
        localVocabularyDataSourceImpl = LocalVocabularyDataSourceImpl(savedWordDAO)
    }


    @Test
    fun `saveWord should return success`() = runBlocking {

        // run
        val result = localVocabularyDataSourceImpl.saveWord(firstEntityInstance.convertToModel())

        // test
        coVerify { savedWordDAO.insertWord(firstEntityInstance) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `saveWord should return failure when exception thrown`() = runBlocking {

        // stub
        coEvery { savedWordDAO.insertWord(any()) } throws RuntimeException("DB error")

        // run
        val result = localVocabularyDataSourceImpl.saveWord(firstEntityInstance.convertToModel())

        // test
        assertTrue(result.isFailure)
    }



    @Test
    fun `deleteWord should return success`() = runBlocking {

        // run
        val result = localVocabularyDataSourceImpl.deleteWord(secondEntityInstance.convertToModel())

        // test
        coVerify { savedWordDAO.deleteWord(secondEntityInstance) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteWord should return failure when exception thrown`() = runBlocking {

        // stub
        coEvery { savedWordDAO.deleteWord(any()) } throws RuntimeException("DB delete error")

        // run
        val result = localVocabularyDataSourceImpl.deleteWord(secondEntityInstance.convertToModel())

        // test
        assertTrue(result.isFailure)
    }



    @Test
    fun `getAllWords should return list of models`() = runBlocking {

        // stub
        val entityList = listOf(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
        coEvery { savedWordDAO.selectAllWords() } returns entityList

        // run
        val result = localVocabularyDataSourceImpl.getAllWords()

        // test
        assertTrue(result.isSuccess)
        assertEquals(
            listOf(
                firstEntityInstance.convertToModel(),
                secondEntityInstance.convertToModel(),
                thirdEntityInstance.convertToModel()
            ),
            result.getOrNull()
        )
    }

    @Test
    fun `getAllWords should return failure when exception thrown`() = runBlocking {

        // stub
        coEvery { savedWordDAO.selectAllWords() } throws RuntimeException("Query error")

        // run
        val result = localVocabularyDataSourceImpl.getAllWords()

        // test
        assertTrue(result.isFailure)
    }
}