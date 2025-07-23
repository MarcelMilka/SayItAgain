package eu.project.localdata.datasource

import eu.project.common.localData.LocalVocabularyDataSource
import eu.project.localdata.dao.SavedWordDAO
import javax.inject.Inject

internal class LocalVocabularyDataSourceImpl @Inject constructor(val savedWordDAO: SavedWordDAO): LocalVocabularyDataSource