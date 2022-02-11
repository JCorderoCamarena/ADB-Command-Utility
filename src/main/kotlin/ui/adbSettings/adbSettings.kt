package ui.adbSettings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.StringRes
import utils.TmpFileNames.ERROR_DISPLAY_TIME_MILLIS
import java.util.prefs.Preferences

private const val KEY_ADB_PATH = "KEY_ADB_PATH"

@Composable
fun ShellAdbSettings() {

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
        adbSettings { error = it }
        ErrorMessage(error)
    }
}

@Composable
fun adbSettings(
    onCommandOutput: (String) -> Unit
) {
    val pref = Preferences.userRoot().node("ShellAdbSettings")
    var adbPathKeySetting by remember { mutableStateOf(pref.get(KEY_ADB_PATH, ADB)) }

    OutlinedTextField(
        value = adbPathKeySetting,
        onValueChange = {
            adbPathKeySetting = it
        },
        label = { Text(text = StringRes.setAdbPathLabel) }
    )

    Button(onClick = {
        pref.put(KEY_ADB_PATH, adbPathKeySetting);
    }) { Text("Save") }
}
