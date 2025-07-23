package eu.project.common.localData

import eu.project.common.model.SavedWord

interface LocalVocabularyDataSource {

    suspend fun saveWord(savedWord: SavedWord): Result<Unit>

    suspend fun deleteWord(savedWord: SavedWord): Result<Unit>

    suspend fun getAllWords(): Result<List<SavedWord>>
}