package ui.localDatabase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import ui.widgets.ErrorMessage
import utils.Constants
import utils.StringRes
import utils.TmpFileNames.LAUNCHER_DATABASE_PATH
import utils.TmpFileNames.LOCAL_DATABASE
import java.io.File

@Composable
fun PullLocalDatabase() {
    var errorMessage by remember { mutableStateOf("") }

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
    try {
        shellRun(command = Constants.ADB , arguments = listOf(Constants.PULL, "$LAUNCHER_DATABASE_PATH/$LOCAL_DATABASE") , workingDirectory = root)
        shellRun(root) { files.openFile(path = LOCAL_DATABASE) }
    } catch (e: ShellRunException) {
        return e.errorText
    }
    return ""
}