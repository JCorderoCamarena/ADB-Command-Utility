package ui.screenshot

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import kotlinx.coroutines.delay
import ui.widgets.ErrorMessage
import utils.Constants.ADB
import utils.Constants.PULL
import utils.Constants.SHELL
import utils.Constants.SIPS
import utils.TmpFileNames.ERROR_DISPLAY_TIME_MILLIS
import java.util.*

@Composable
fun ShellScreenShot() {

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
        adbScreenshotButton { error = it }
        ErrorMessage(error)
    }
}

@Composable
fun adbScreenshotButton(
    onCommandOutput: (String) -> Unit
) {
    Button(onClick = {
        onCommandOutput(sendScreenshotCommand())
    }) {
        Icon(
            FeatherIcons.Camera,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Screenshot")
    }
}

fun sendScreenshotCommand(): String {
    val screenCapCommandOne = "screencap"
    val screenCapAttributeOne = "-p"

    val extension = ".png"
    val fileName = "android_screenshot-${UUID.randomUUID()}$extension"
    val screenshotFileSrc = "/sdcard/$fileName"
    val screenshotFileTgt = ShellLocation.HOME.resolve("Desktop").path

    try {
        shellRun {
            command(
                command = ADB,
                arguments = listOf(SHELL, screenCapCommandOne, screenCapAttributeOne, screenshotFileSrc)
            )
            command(command = ADB, arguments = listOf(PULL, screenshotFileSrc, screenshotFileTgt))
            command(command = SIPS, listOf("-r", "-180", "$screenshotFileTgt/$fileName"))
        }
    } catch (e: ShellRunException) {
        return e.errorText
    } catch (shellExc: ShellFailedException) {
        return shellExc.cause?.message ?: shellExc.localizedMessage
    }
    return ""
}