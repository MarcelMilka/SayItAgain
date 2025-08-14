package eu.project.saved.exportWords.state

import eu.project.saved.exportWords.model.ExportMethod
import eu.project.saved.exportWords.model.ExportMethodVariants

data class ExportMethodControllerState(
    val exportMethod: ExportMethod = ExportMethod.NotSpecified,
    val sendMethodState: ExportMethodState = ExportMethodVariants.sendNotSelected,
    val downloadMethodState: ExportMethodState = ExportMethodVariants.downloadNotSelected,
)