package eu.project.sayitagain

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import eu.project.common.localData.SavedWordsRepository
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {

    @Inject lateinit var savedWordsRepository: SavedWordsRepository

    override fun onCreate() {
        super.onCreate()

        savedWordsRepository.dataState
    }
}