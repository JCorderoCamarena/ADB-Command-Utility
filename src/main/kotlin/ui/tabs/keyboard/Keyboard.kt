package ui.tabs.keyboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import compose.icons.FeatherIcons
import compose.icons.feathericons.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import utils.Constants

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeyboardScreen(
    keypadItems: List<CustomKey> = listOf(
        CustomKey.Empty,
        CustomKey.UpKey,
        CustomKey.Empty,
        CustomKey.LeftKey,
        CustomKey.CenterKey,
        CustomKey.RightKey,
        CustomKey.Empty,
        CustomKey.DownKey,
        CustomKey.Empty,
    ),
    additionalKeys: List<CustomKey> = listOf(
        CustomKey.BackKey,
        CustomKey.HomeKey,
        CustomKey.PowerKey
    )
) {
    LazyVerticalGrid(
        modifier = Modifier.width(260.dp),
        cells = GridCells.Adaptive(80.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 12.dp,
            end = 12.dp,
            bottom = 12.dp
        ),
        content = {
            items(keypadItems.size) { index ->
                if (keypadItems[index].event < 0) {
                    EmptyComposable()
                } else {
                    KeypadButtton(keypadItems[index].value, keypadItems[index].event)
                }
            }

            items(additionalKeys.size) { index ->
                KeypadButtton(additionalKeys[index].value, additionalKeys[index].event)
            }
        }
    )
}


@Composable
fun KeypadButtton(icon: ImageVector?, event: Int) {
    val scope = rememberCoroutineScope()
    OutlinedButton(
        modifier = Modifier
            .padding(4.dp),
        onClick = {
            println("adb shell input keyevent $event")
            scope.launch(IO) {
                onKeyPressed(event)
            }
            println("Working")
        }
    ) {
        icon?.let {
            Icon(it, contentDescription = "Localized description")
        }
    }
}

@Composable
fun EmptyComposable() {
    Column(modifier = Modifier.padding(4.dp)) { }
}


fun onKeyPressed(event: Int): String {
    return shellRun {
        try {
            command(
                command = Constants.ADB,
                arguments = listOf(Constants.SHELL, Constants.INPUT, Constants.KEY_EVENT, "$event")
            )
            ""
        } catch (e: ShellRunException) {
            e.errorText
        } catch (shellExc: ShellFailedException) {
            shellExc.cause?.message ?: shellExc.localizedMessage
        }
    }
}


sealed class CustomKey(val value: ImageVector?, val event: Int) {
    object HomeKey: CustomKey(FeatherIcons.Home, 3)
    object BackKey: CustomKey(FeatherIcons.CornerUpLeft, 4)
    object UpKey: CustomKey(FeatherIcons.ArrowUp, 19)
    object DownKey: CustomKey(FeatherIcons.ArrowDown, 20)
    object LeftKey: CustomKey(FeatherIcons.ArrowLeft, 21)
    object RightKey: CustomKey(FeatherIcons.ArrowRight, 22)
    object CenterKey: CustomKey(FeatherIcons.StopCircle, 23)
    object PowerKey: CustomKey(FeatherIcons.Power, 26)
    object Empty: CustomKey(null, -1)
}