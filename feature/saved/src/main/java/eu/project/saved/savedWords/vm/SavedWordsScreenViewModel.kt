package eu.project.saved.savedWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.model.SavedWordsScreenViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SavedWordsScreenViewModel @Inject constructor(private val savedWordsRepository: SavedWordsRepository): ViewModel() {

    private val _uiState = MutableStateFlow<SavedWordsScreenViewState>(SavedWordsScreenViewState.Loading)
    val uiState = _uiState.asStateFlow()

    init {

        viewModelScope.launch {

            savedWordsRepository
                .dataState
                .collect {

                    when(it) {

                        SavedWordsRepositoryDataState.Loading ->
                            _uiState.value = SavedWordsScreenViewState.Loading

                        SavedWordsRepositoryDataState.Loaded.NoData ->
                            _uiState.value = SavedWordsScreenViewState.Loaded.NoData

                        is SavedWordsRepositoryDataState.Loaded.Data ->
                            _uiState.value = SavedWordsScreenViewState.Loaded.Data(retrievedData = it.retrievedData)

                        is SavedWordsRepositoryDataState.FailedToLoad ->
                            _uiState.value = SavedWordsScreenViewState.FailedToLoad(cause = it.cause)
                    }
                }
        }
    }

    fun deleteWord(wordToDelete: SavedWord) {

        viewModelScope.launch {

            savedWordsRepository.deleteWord(wordToDelete = wordToDelete)
        }
    }
}