package eu.project.saved.exportWords.model

data class ExportMethodPickerState(
    val exportMethod: ExportMethod = ExportMethod.NotSpecified,
    val sendMethodState: ExportMethodState = ExportMethodVariants.sendNotSelected,
    val downloadMethodState: ExportMethodState = ExportMethodVariants.downloadNotSelected,
)