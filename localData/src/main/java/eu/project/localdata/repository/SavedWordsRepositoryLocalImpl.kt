package eu.project.localdata.repository

import eu.project.common.localData.SavedWordsDataSource
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class SavedWordsRepositoryLocalImpl(
    private val ioCoroutineScope: CoroutineScope,
    private val savedWordsDataSource: SavedWordsDataSource
): SavedWordsRepository {

    private val _dataState = MutableStateFlow<SavedWordsRepositoryDataState>(SavedWordsRepositoryDataState.Loading)
    override val dataState = _dataState.asStateFlow()

    init {

        ioCoroutineScope.launch {

            getAllWords()
        }
    }

    override suspend fun saveWord(wordToSave: SavedWord) {

        val doOnSuccess: (Unit) -> Unit = {}
        val doOnFailure: (Throwable) -> Unit = {}

        savedWordsDataSource.saveWord(wordToSave)
            .onSuccess(doOnSuccess)
            .onFailure(doOnFailure)
    }

    override suspend fun deleteWord(wordToDelete: SavedWord) {

        val doOnSuccess: (Unit) -> Unit = {}
        val doOnFailure: (Throwable) -> Unit = {}

        savedWordsDataSource.deleteWord(wordToDelete)
            .onSuccess(doOnSuccess)
            .onFailure(doOnFailure)
    }

    override suspend fun getAllWords() {

        val doOnSuccess: (List<SavedWord>) -> Unit = {}
        val doOnFailure: (Throwable) -> Unit = {}

        savedWordsDataSource.getAllWords()
            .onSuccess(doOnSuccess)
            .onFailure(doOnFailure)
    }
}