package ui.tabs.download.fileList

import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class FileListController(
    private val scope: CoroutineScope = CoroutineScope(IO)
) {

    fun getFullFileList(
        onFileList: (List<LogFileItem>) -> Unit
    ) {
        scope.launch {
            val logs = getLogs()
            val databases = getDatabases()

            val output: MutableList<LogFileItem> = mutableListOf()
            if (logs.lines().isNotEmpty()) {
                output.addAll(
                    logs.lines().map {
                        LogFileItem(path = "/data", fileName = it)
                    }
                )
            }

            if (databases.isNotEmpty() && databases.lines().isNotEmpty()) {
                output.addAll(
                    databases
                        .lines()
                        .filter { it.endsWith(".db") }
                        .map {
                            LogFileItem(
                                path = "/data/data/com.midtronics.android.launcher/databases",
                                fileName = it
                            )
                        }
                )
            }
            onFileList(output)
        }
    }

    fun getDatabases(): String {
        val databases = shellRun {
            try {
                // Check if devices is connected
                command("adb", listOf(
                    "shell",
                    "ls",
                    "/data/data/com.midtronics.android.launcher/databases"
                ))
            } catch (runExc: ShellRunException) {
                println(runExc.errorText)
                ""
            } catch (shellExc: ShellFailedException) {
                println(shellExc.cause?.message ?: shellExc.localizedMessage)
                ""
            }
        }

        return databases
    }

    fun getLogs(): String {
        val logs = shellRun {
            try {
                // Check if devices is connected
                command("adb", listOf(
                    "shell",
                    "ls",
                    "/data",
                    "|",
                    "grep",
                    "\"^log.*\$\"",
                ))
            } catch (runExc: ShellRunException) {
                println(runExc.errorText)
                ""
            } catch (shellExc: ShellFailedException) {
                println(shellExc.cause?.message ?: shellExc.localizedMessage)
                ""
            }
        }

        return logs
    }

    fun downloadSelectedFiles(root: File, filteredList: List<LogFileItem>, onFinishDownloading: () -> Unit) {
        scope.launch {
            val directoryName = "files-${System.currentTimeMillis()}"
            shellRun(root) {
                command("mkdir", listOf(directoryName))
            }
            filteredList.forEach {
                println(it)
                shellRun(root) {
                    command("adb", listOf("pull", "${it.path}/${it.fileName}", directoryName))
                }
            }
            onFinishDownloading()
        }
    }

}