package eu.project.saved.savedWords.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import eu.project.common.localData.SavedWordsRepository
import eu.project.common.localData.SavedWordsRepositoryDataState
import eu.project.common.model.SavedWord
import eu.project.saved.savedWords.state.DiscardWordDialogState
import eu.project.saved.savedWords.state.SavedWordsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SavedWordsScreenViewModel @Inject constructor(private val savedWordsRepository: SavedWordsRepository): ViewModel() {

    private val _screenState = MutableStateFlow<SavedWordsScreenState>(SavedWordsScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _dialogState = MutableStateFlow<DiscardWordDialogState>(DiscardWordDialogState.Hidden)
    val dialogState = _dialogState.asStateFlow()

    init {

        viewModelScope.launch {

            savedWordsRepository
                .dataState
                .collect {

                    when(it) {

                        SavedWordsRepositoryDataState.Loading ->
                            _screenState.value = SavedWordsScreenState.Loading

                        SavedWordsRepositoryDataState.Loaded.NoData ->
                            _screenState.value = SavedWordsScreenState.Loaded.NoData

                        is SavedWordsRepositoryDataState.Loaded.Data ->
                            _screenState.value = SavedWordsScreenState.Loaded.Data(retrievedData = it.retrievedData)

                        is SavedWordsRepositoryDataState.FailedToLoad ->
                            _screenState.value = SavedWordsScreenState.Error(cause = it.cause)
                    }
                }
        }
    }

    fun requestWordDeletion(wordToDelete: SavedWord) {

        viewModelScope.launch {

            _dialogState.value = DiscardWordDialogState.Visible(wordToDelete)
        }
    }

    fun cancelWordDeletion() {

        viewModelScope.launch {

            _dialogState.value = DiscardWordDialogState.Hidden
        }
    }

    fun deleteWord(wordToDelete: SavedWord) {

        viewModelScope.launch {

            savedWordsRepository.deleteWord(wordToDelete = wordToDelete)
            _dialogState.value = DiscardWordDialogState.Hidden
        }
    }
}