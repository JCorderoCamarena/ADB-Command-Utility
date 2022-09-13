package ui.tabs.download.fileList

sealed class OutputState(val message: String) {
    object Default: OutputState("")
    class Error(message: String): OutputState(message)
    class Success(message: String): OutputState(message)
}
