package ui.inputText

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.INPUT
import utils.Constants.SHELL
import utils.Constants.TEXT
import utils.StringRes

@Composable
fun ShellInputTextField() {

    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ADBInputTextField { error = it }
        ErrorMessage(error)
    }
}

@Composable
fun ADBInputTextField(
    onCommandOutput: (String) -> Unit
) {
    var value by remember { mutableStateOf("") }

    OutlinedTextField(
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
    }
    return ""
}