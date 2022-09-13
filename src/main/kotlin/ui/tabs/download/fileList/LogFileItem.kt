package ui.tabs.download.fileList

data class LogFileItem(
    val path: String,
    val fileName: String,
    var isChecked: Boolean = false
)
