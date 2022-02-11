package ui.inputText

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import kotlinx.coroutines.delay
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.INPUT
import utils.Constants.SHELL
import utils.Constants.TEXT
import utils.StringRes
import utils.TmpFileNames.ERROR_DISPLAY_TIME_MILLIS

@Composable
fun ShellInputTextField() {

    var error by remember { mutableStateOf("") }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            delay(ERROR_DISPLAY_TIME_MILLIS)
            error = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ADBInputTextField { error = it }
        ErrorMessage(error)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ADBInputTextField(
    onCommandOutput: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier.onKeyEvent {
            return@onKeyEvent when(it.key) {
              Key.Enter -> {
                  onCommandOutput(sendTextCommand(value))
                  true
              }
              else -> false
            }
        },
        value = value,
        onValueChange = {
            onCommandOutput("")
            value = it
        },
        label = {
            Text(StringRes.enterInputText)
        },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = {
                onCommandOutput(sendTextCommand(value))
            }) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        }
    )
}

fun sendTextCommand(value: String): String {
    try {
        shellRun(command = ADB, arguments = listOf(SHELL, INPUT, TEXT, value))
    } catch (e: ShellRunException) {
        return e.errorText
    } catch (shellExc: ShellFailedException) {
        return shellExc.cause?.message ?: shellExc.localizedMessage
    }
    return ""
}
