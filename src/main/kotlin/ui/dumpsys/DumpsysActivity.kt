package ui.dumpsys

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import kotlinx.coroutines.delay
import ui.widgets.ErrorMessage
import utils.Constants.ACTIVITY
import utils.Constants.ADB
import utils.Constants.DUMPSYS
import utils.Constants.SHELL
import utils.StringRes
import utils.TmpFileNames.ERROR_DISPLAY_TIME_MILLIS

@Composable
fun DumpsysActivitySection() {
    var output by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(error) {
        if (error.isNotEmpty()) {
            delay(ERROR_DISPLAY_TIME_MILLIS)
            error = ""
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        DumpsysActivityButton(
            onLogRetrieved = { output = it },
            onError = { error = it },
            onClearPressed = { output = "" }
        )

        DumpsysLog(output)

        ErrorMessage(error)
    }

}

@Composable
fun DumpsysActivityButton(
    onLogRetrieved: (String) -> Unit,
    onError: (String) -> Unit,
    onClearPressed: () -> Unit
) {
    var currentActivityOnly by remember { mutableStateOf(true) }

    Row(modifier = Modifier.fillMaxWidth()) {
        Button(onClick = {
            runDumpsysActivity(currentActivityOnly, onLogRetrieved = onLogRetrieved, onError = onError)
        }) {
            Text(StringRes.runDumpsys)
        }

        ClearButton(
            onClearPressed = onClearPressed
        )

        Checkbox(
            modifier = Modifier.padding(start = 15.dp),
            checked = currentActivityOnly,
            onCheckedChange = {
                currentActivityOnly = !currentActivityOnly
            }
        )
        Text(
            text = StringRes.runDumpsysCheckboxLabel,
            style = MaterialTheme.typography.overline
        )

    }
}

@Composable
fun ClearButton(
    onClearPressed: () -> Unit
) {
    Button(
        onClick = onClearPressed,
        modifier = Modifier.padding(start = 10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
    ) {
        Text(
            StringRes.clearDumpsys,
            color = MaterialTheme.colors.onError
        )
    }
}

fun runDumpsysActivity(
    currentActivityOnly: Boolean,
    onLogRetrieved: (String) -> Unit,
    onError: (String) -> Unit
) {
    try {
        var output = shellRun(command = ADB, arguments = listOf(SHELL, DUMPSYS, ACTIVITY))
        if (currentActivityOnly) {
            println("Get Dumpsys for specific activity")
            val currentActivity = getCurrentActivity(output)
            println(currentActivity)
            if (currentActivity.isNotEmpty()) {
                output = shellRun(command = ADB, arguments = listOf(SHELL, DUMPSYS, ACTIVITY, currentActivity))
            }
        }
        onLogRetrieved(output)
    } catch (runExc: ShellRunException) {
        onError(runExc.errorText)
    } catch (failedExc: ShellFailedException) {
        val error = failedExc.cause?.message ?: failedExc.localizedMessage
        onError(error)
    }
}

@Composable
fun DumpsysLog(dumpsys: String) {

    AnimatedVisibility(visible = dumpsys.isNotEmpty()) {
        var scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .background(color = Color.Black.copy(alpha = 0.5f))
                .border(border = BorderStroke(1.dp, Color.Black))
                .fillMaxWidth()
                .height(TextFieldDefaults.MinHeight * 8)
                .verticalScroll(state = scrollState)
        ) {
            SelectionContainer {
                Text(
                    modifier = Modifier.padding(15.dp),
                    text = dumpsys,
                    style = MaterialTheme.typography.overline,
                    color = Color.White
                )
            }
        }
    }
}

fun getCurrentActivity(output: String): String {
    val lineFound = findLine(output)

    return when {
        lineFound.isEmpty() -> lineFound
        else -> getActivityName(lineFound)
    }
}

fun getActivityName(lineFound: String): String {
    //  mResumedActivity: ActivityRecord{4230ddd0 u0 com.midtronics.android.launcher/com.midtronics.android.fordapps.invehicle.FordInVehicleChargeActivity t26}
    val substringPos = findIndexOfNthOccurrenceInString(lineFound, "com", 1)
    println("SubstringPos: $substringPos")
    return when {
        substringPos > 0 -> {
            val output = lineFound.substring(startIndex = substringPos).substringBefore(" ")
            output
        }
        else -> ""
    }
}

fun findLine(output: String): String {
    val filter = "mFocusedActivity"
    for (line in output.lines()) {
        if (line.contains(filter)) {
            println("found: $line" )
            return line
        }
    }
    return ""
}

// Zero-based
fun findIndexOfNthOccurrenceInString(input: String, substring: String, nth: Int): Int {
    var pos = -1
    do {
        pos = input.indexOf(substring, pos + 1)
    } while (nth.dec() > 0 && pos != -1)
    return pos
}


