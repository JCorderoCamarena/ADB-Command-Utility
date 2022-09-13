// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.icons.FeatherIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.Grid
import ui.adbSettings.ShellAdbSettings
import ui.dumpsys.DumpsysActivitySection
import ui.inputText.ShellInputTextField
import ui.tabs.download.localDatabase.PullLocalDatabase
import ui.tabs.download.localDatabase.PushLocalDatabase
import ui.screenshot.ShellScreenShot
import ui.setserial.SerialNumber
import ui.tabs.download.fileList.PullLogs
import ui.tabs.keyboard.KeyboardScreen
import utils.StringRes

@Composable
@Preview
fun App() {

    var screen by remember { mutableStateOf(ScreenTab.COMMANDS) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    // Leading icons should typically have a high content alpha
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == ScreenTab.COMMANDS) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = ScreenTab.COMMANDS }) {
                            Icon(Icons.Filled.Home, contentDescription = "Localized description")
                        }
                    }
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == ScreenTab.DB) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = ScreenTab.DB }) {
                            Icon(FeatherIcons.Download, contentDescription = "Localized description")
                        }
                    }
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == ScreenTab.KEYBOARD) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = ScreenTab.KEYBOARD }) {
                            Icon(FeatherIcons.Grid, contentDescription = "Localized description")
                        }
                    }

                    // The actions should be at the end of the BottomAppBar. They use the default medium
                    // content alpha provided by BottomAppBar
                    Spacer(Modifier.weight(1f, true))
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == ScreenTab.SETTINGS) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = ScreenTab.SETTINGS }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Localized description")
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                when(screen) {
                    ScreenTab.SETTINGS -> {
                        ShellAdbSettings()
                    }
                    ScreenTab.COMMANDS -> {
                        ShellInputTextField()
                        SerialNumber()
                        ShellScreenShot()
                        DumpsysActivitySection()
                    }
                    ScreenTab.DB -> {
                        PullLocalDatabase()
                        PushLocalDatabase()
                        PullLogs()
                    }
                    ScreenTab.KEYBOARD -> {
                        KeyboardScreen()
                    }
                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = StringRes.appTitle,
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}

enum class ScreenTab {
    SETTINGS,
    COMMANDS,
    DB,
    KEYBOARD
}

