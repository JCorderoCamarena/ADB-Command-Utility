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
import kotlinx.coroutines.delay
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.AM
import utils.Constants.FORCE_STOP
import utils.Constants.PUSH
import utils.Constants.RM
import utils.Constants.SHELL
import utils.Constants._F
import utils.StringRes
import utils.TmpFileNames.LAUNCHER_DATABASE_PATH
import utils.TmpFileNames.LAUNCHER_PACKAGE
import utils.TmpFileNames.LOCAL_DATABASE
import utils.TmpFileNames.LOCAL_DATABASE_JOURNAL
import java.io.File

@Composable
fun PushLocalDatabase() {

    var error by remember { mutableStateOf("") }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            delay(3000)
            error = ""
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        PushLocalDatabaseButton{ error = it }
        ErrorMessage(error)
    }
}

@Composable
fun PushLocalDatabaseButton(
    onButtonPressed: (String) -> Unit
) {
    Button(
        modifier = Modifier.height(TextFieldDefaults.MinHeight),
        onClick =  {
            val res = when(hostOs) {
                OS.Windows -> TODO()
                OS.MacOS -> runPushDatabaseCommand(ShellLocation.HOME.resolve("Desktop"))
                else -> ""
            }
            onButtonPressed(res)
        }
    ) {
        Text(StringRes.pushLocalDatabse)
    }
}

fun runPushDatabaseCommand(root: File): String {
    return shellRun(root) {
        try {
            command(
                command = ADB,
                arguments = listOf(SHELL, RM, _F, "$LAUNCHER_DATABASE_PATH/$LOCAL_DATABASE")
            )

            command(
                command = ADB,
                arguments = listOf(SHELL, RM, _F, LOCAL_DATABASE_JOURNAL)
            )

            command(
                command = ADB,
                arguments = listOf(PUSH, LOCAL_DATABASE, LAUNCHER_DATABASE_PATH)
            )

            command(
                command = ADB,
                arguments = listOf(SHELL, AM, FORCE_STOP, LAUNCHER_PACKAGE)
            )
            ""
        } catch (e: ShellRunException) {
            e.errorText
        }
    }
}
