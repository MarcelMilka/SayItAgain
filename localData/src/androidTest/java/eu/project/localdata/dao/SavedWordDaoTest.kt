package eu.project.localdata.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import eu.project.localdata.database.ApplicationDatabase
import eu.project.localdata.entity.SavedWordEntity
import junit.framework.Assert.assertTrue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.UUID

internal class SavedWordDaoTest {

    private lateinit var applicationDatabase: ApplicationDatabase
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

        val context = ApplicationProvider.getApplicationContext<Context>()

        applicationDatabase = Room.inMemoryDatabaseBuilder(
            context,
            ApplicationDatabase::class.java
        ).build()

        savedWordDAO = applicationDatabase.savedWordDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {

        applicationDatabase.clearAllTables()
        applicationDatabase.close()
    }



    @Test
    fun insertWord() = runBlocking {

        // add a word
        savedWordDAO.insertWord(firstEntityInstance)

        // make sure the word has been added
        val expected = listOf(firstEntityInstance)
        val actual = savedWordDAO.selectAllWords()

        assertEquals(expected, actual)
    }


    @Test
    fun insertWord_shouldReplaceEntityWithSameUUID() = runBlocking {

        // add a word
        savedWordDAO.insertWord(firstEntityInstance)

        // add a word with the same UUID as firstEntityInstance
        val updatedInstance = firstEntityInstance.copy(
            word = "Updated Cat",
            language = "Norwegian"
        )
        savedWordDAO.insertWord(updatedInstance)

        // make sure the values have been replaced
        val expected = listOf(updatedInstance)
        val actual = savedWordDAO.selectAllWords()

        assertEquals(expected, actual)
    }

    @Test
    fun insertMultipleWords_shouldNotGuaranteeOrder() = runBlocking {

        // add words
        savedWordDAO.insertWord(secondEntityInstance)
        savedWordDAO.insertWord(firstEntityInstance)
        savedWordDAO.insertWord(thirdEntityInstance)

        val actual = savedWordDAO.selectAllWords()

        assertTrue(actual.containsAll(listOf(firstEntityInstance, secondEntityInstance, thirdEntityInstance)))
        assertEquals(3, actual.size)
    }

    @Test
    fun deleteWord() = runBlocking {

        // add words
        savedWordDAO.insertWord(firstEntityInstance)
        savedWordDAO.insertWord(secondEntityInstance)

        // make sure the words have been added
        val expected = listOf(firstEntityInstance, secondEntityInstance)
        val actual = savedWordDAO.selectAllWords()

        assertEquals(expected, actual)

        // delete firstEntityInstance
        savedWordDAO.deleteWord(firstEntityInstance)

        // make sure the word has been added
        val expected2 = listOf(secondEntityInstance)
        val actual2 = savedWordDAO.selectAllWords()

        assertEquals(expected2, actual2)
    }

    @Test
    fun deleteNonExistentWord_shouldNotCrash() = runBlocking {

        // should not crash
        savedWordDAO.deleteWord(firstEntityInstance)
        val actual = savedWordDAO.selectAllWords()
        assertTrue(actual.isEmpty())
    }

    @Test
    fun selectAllWords() = runBlocking {

        // add words
        savedWordDAO.insertWord(firstEntityInstance)
        savedWordDAO.insertWord(secondEntityInstance)
        savedWordDAO.insertWord(thirdEntityInstance)

        // make sure the words have been added
        val expected = listOf(firstEntityInstance, secondEntityInstance, thirdEntityInstance)
        val actual = savedWordDAO.selectAllWords()

        assertEquals(expected, actual)
    }
}