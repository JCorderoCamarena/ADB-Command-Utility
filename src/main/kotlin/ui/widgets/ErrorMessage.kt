package ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import ui.tabs.download.fileList.OutputState
import utils.TmpFileNames

@Composable
fun ErrorMessage(error: String) {
    AnimatedVisibility(visible = error.isNotEmpty()) {
        Text(
            text = error,
            color = MaterialTheme.colors.error
        )
    }
}

@Composable
fun OutputMessage(outputState: OutputState, onResetState: () -> Unit) {

    LaunchedEffect(outputState.message) {
        if (outputState.message.isNotEmpty()) {
            println("Message is shown")
            delay(TmpFileNames.ERROR_DISPLAY_TIME_MILLIS)
            println("Message is hidden")
            onResetState()
        }
    }

    AnimatedVisibility(visible = outputState.message.isNotEmpty()) {
        val fontColor = when(outputState) {
            is OutputState.Error -> MaterialTheme.colors.error
            else -> MaterialTheme.colors.primary
        }

        Text(
            text = outputState.message,
            color = fontColor
        )
    }
}