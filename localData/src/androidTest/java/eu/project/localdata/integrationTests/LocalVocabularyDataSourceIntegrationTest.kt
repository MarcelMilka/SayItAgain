package eu.project.localdata.integrationTests

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import eu.project.common.localData.LocalVocabularyDataSource
import eu.project.localdata.dao.SavedWordDAO
import eu.project.localdata.database.ApplicationDatabase
import eu.project.localdata.datasource.LocalVocabularyDataSourceImpl
import eu.project.localdata.entity.SavedWordEntity
import eu.project.localdata.entity.convertToModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.UUID

class LocalVocabularyDataSourceIntegrationTest {

    private lateinit var applicationDatabase: ApplicationDatabase
    private lateinit var savedWordDAO: SavedWordDAO
    private lateinit var dataSource: LocalVocabularyDataSource

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

        dataSource = LocalVocabularyDataSourceImpl(savedWordDAO)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {

        applicationDatabase.clearAllTables()
        applicationDatabase.close()
    }



    @Test
    fun saveWord_shouldAddWordToDatabase() = runBlocking {

        // test
        val result = dataSource.saveWord(firstEntityInstance.convertToModel())
        assertTrue(result.isSuccess)

        val saved = savedWordDAO.selectAllWords()
        assertEquals(listOf(firstEntityInstance), saved)
    }



    @Test
    fun getAllWords_shouldReturnAllSavedWords() = runBlocking {

        // set up
        savedWordDAO.insertWord(firstEntityInstance)
        savedWordDAO.insertWord(secondEntityInstance)

        // test
        val result = dataSource.getAllWords()

        assertTrue(result.isSuccess)
        assertEquals(
            listOf(firstEntityInstance.convertToModel(), secondEntityInstance.convertToModel()),
            result.getOrNull()
        )
    }



    @Test
    fun deleteWord_shouldRemoveWordFromDatabase() = runBlocking {

        // set up
        savedWordDAO.insertWord(firstEntityInstance)
        savedWordDAO.insertWord(secondEntityInstance)


        // test
        val result = dataSource.deleteWord(firstEntityInstance.convertToModel())
        assertTrue(result.isSuccess)

        val remaining = savedWordDAO.selectAllWords()
        assertEquals(listOf(secondEntityInstance), remaining)
    }



    @Test
    fun insertWord_withSameUUID_shouldReplaceExisting() = runBlocking {

        // set up
        val original = firstEntityInstance
        val updated = original.copy(word = "Updated Cat")

        dataSource.saveWord(original.convertToModel())
        dataSource.saveWord(updated.convertToModel())

        // test
        val saved = savedWordDAO.selectAllWords()

        assertEquals(1, saved.size)
        assertEquals(updated, saved.first())
    }



    @Test
    fun getAllWords_shouldReturnEmptyListIfNoneExist() = runBlocking {

        // set up
        val result = dataSource.getAllWords()

        // test
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }
}