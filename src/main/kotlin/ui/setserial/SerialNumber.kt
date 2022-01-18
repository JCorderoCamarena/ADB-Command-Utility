package ui.setserial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import kotlinx.coroutines.delay
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.DSS_SERIAL
import utils.Constants.SETPROP
import utils.Constants.SHELL
import utils.StringRes
import utils.TmpFileNames.ERROR_DISPLAY_TIME_MILLIS

@Composable
fun SerialNumber() {
    var error by remember { mutableStateOf("") }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            delay(ERROR_DISPLAY_TIME_MILLIS)
            error = ""
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SerialNumberInputText { error = it }
        ErrorMessage(error)
    }
}


@Composable
fun SerialNumberInputText(
    onSerialEntered: (String) -> Unit
) {
    var serial by remember { mutableStateOf("") }

    OutlinedTextField(
        label = {
            Text(StringRes.enterSerial)
        },
        value = serial,
        onValueChange = {
            serial = it
        },
        trailingIcon = {
            IconButton(
                enabled = serial.length == 10 && serial.toIntOrNull() != null,
                onClick = {
                    val output = onSetSerial(serial)
                    if (output.isEmpty()) {
                        serial = ""
                    }
                    onSerialEntered(output)
                }
            ) {
                Icon(imageVector = Icons.Filled.Send , contentDescription = null)
            }
        },
        maxLines = 1
    )
}

fun onSetSerial(serial: String): String {
    return shellRun {
        try {
            command(command = ADB, arguments = listOf(SHELL, SETPROP, DSS_SERIAL, serial))
        } catch (e: ShellRunException) {
            e.errorText
        }
    }
}