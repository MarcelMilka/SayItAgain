package eu.project.sayitagain

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import eu.project.common.eventBus.EventBus
import eu.project.common.eventBus.SaveFileEventBusQualifier
import eu.project.common.eventBus.saveFile.SaveFileEvent
import eu.project.common.remoteData.CsvFile
import eu.project.scaffold.applicationScaffold
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    @SaveFileEventBusQualifier
    lateinit var saveFileEventBus: EventBus<SaveFileEvent>

    private var csvFile: CsvFile? = null

    private val saveFileLauncher = registerForActivityResult(contract = SaveFileContract()) { uri: Uri? ->

        if (uri != null && csvFile != null) {

            contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(csvFile!!.data)
            }

            lifecycleScope.launch {

                saveFileEventBus.emit(SaveFileEvent.FileSavedSuccessfully)
            }
        }

        else {

            lifecycleScope.launch {

                val event = SaveFileEvent.SaveFileError(Throwable("Both CsvFile and URI must not be null"))
                saveFileEventBus.emit(event)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            applicationScaffold()
        }

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                saveFileEventBus.events.collect { event ->

                    if (event is SaveFileEvent.SaveFile) {

                        saveFile(newCsvFile = event.csvFile)
                    }
                }
            }
        }
    }

    private fun saveFile(newCsvFile: CsvFile) {

        csvFile = newCsvFile
        saveFileLauncher.launch(input = Unit)
    }
}