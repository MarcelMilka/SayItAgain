package eu.project.localdata.datasource

import eu.project.common.localData.SavedWordsDataSource
import eu.project.common.model.SavedWord
import eu.project.localdata.dao.SavedWordDAO
import eu.project.localdata.entity.convertToEntity
import eu.project.localdata.entity.convertToModel
import javax.inject.Inject

internal class SavedWordsDataSourceLocalImpl @Inject constructor(val savedWordDAO: SavedWordDAO): SavedWordsDataSource {

    override suspend fun saveWord(word: SavedWord): Result<Unit> {

        return try {

            savedWordDAO.insertWord(word.convertToEntity())
            Result.success(Unit)
        }

        catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun deleteWord(word: SavedWord): Result<Unit> {

        return try {

            savedWordDAO.deleteWord(word.convertToEntity())
            Result.success(Unit)
        }

        catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun getAllWords(): Result<List<SavedWord>> {

        return try {

            val retrievedWords =
                savedWordDAO
                .selectAllWords()
                .map { it.convertToModel() }

            Result.success(retrievedWords)
        }

        catch (e: Exception) {

            Result.failure(e)
        }
    }
}