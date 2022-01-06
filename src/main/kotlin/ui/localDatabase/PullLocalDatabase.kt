package ui.localDatabase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lordcodes.turtle.*
import kotlinx.coroutines.delay
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.DEVICES
import utils.Constants.PULL
import utils.StringRes
import utils.TmpFileNames.LAUNCHER_DATABASE_PATH
import utils.TmpFileNames.LOCAL_DATABASE
import java.io.File

@Composable
fun PullLocalDatabase() {
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            println("Error is shown")
            delay(3000)
            errorMessage = ""
            println(println("Error is hidden"))
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        PullLocalDatabaseButton { errorMessage = it }
        ErrorMessage(errorMessage)
    }
}

@Composable
fun PullLocalDatabaseButton(
    onButtonPressed: (String) -> Unit
) {
    Button(
        modifier = Modifier.height(TextFieldDefaults.MinHeight),
        onClick = {
            val errorMessage = when(hostOs) {
                OS.Windows -> {
                    TODO()
                }
                OS.MacOS -> {
                    val root = ShellLocation.HOME.resolve("Desktop")
                    pullLocalDatabase(root)
                }
                else -> ""
            }
            onButtonPressed(errorMessage)
        }
    ) {
        Text(StringRes.pullLocalDatabse)
    }
}

fun pullLocalDatabase(root: File): String {
    return shellRun(root) {
        try {
            // Check if devices is connected
            val devices = command(command = ADB, arguments = listOf(DEVICES))
            var output = command(command = ADB , arguments = listOf(PULL, "$LAUNCHER_DATABASE_PATH/$LOCAL_DATABASE"))
            if (devices.lines().size > 1) {
                output = files.openFile(path = LOCAL_DATABASE)
            }
            output
        } catch (runExc: ShellRunException) {
            runExc.errorText
        } catch (shellExc: ShellFailedException) {
            shellExc.cause?.message ?: shellExc.localizedMessage
        }
    }
}