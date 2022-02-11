// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.icons.AllIcons
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.Database
import compose.icons.feathericons.Download
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Database
import ui.adbSettings.ShellAdbSettings
import ui.dumpsys.DumpsysActivitySection
import ui.inputText.ShellInputTextField
import ui.localDatabase.PullLocalDatabase
import ui.localDatabase.PushLocalDatabase
import ui.screenshot.ShellScreenShot
import ui.setserial.SerialNumber
import utils.StringRes

@Composable
@Preview
fun App() {

    var screen by remember { mutableStateOf(Screen.COMMANDS) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomAppBar {
                    // Leading icons should typically have a high content alpha
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == Screen.COMMANDS) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = Screen.COMMANDS }) {
                            Icon(Icons.Filled.Home, contentDescription = "Localized description")
                        }
                    }
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == Screen.DB) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = Screen.DB }) {
                            Icon(FeatherIcons.Download, contentDescription = "Localized description")
                        }
                    }

                    // The actions should be at the end of the BottomAppBar. They use the default medium
                    // content alpha provided by BottomAppBar
                    Spacer(Modifier.weight(1f, true))
                    CompositionLocalProvider(LocalContentAlpha provides if (screen == Screen.SETTINGS) ContentAlpha.high else ContentAlpha.medium) {
                        IconButton(onClick = { screen = Screen.SETTINGS }) {
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
                    Screen.SETTINGS -> {
                        ShellAdbSettings()
                    }
                    Screen.COMMANDS -> {
                        ShellInputTextField()
                        SerialNumber()
                        ShellScreenShot()
                        DumpsysActivitySection()
                    }
                    Screen.DB -> {
                        PullLocalDatabase()
                        PushLocalDatabase()
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

enum class Screen {
    SETTINGS,
    COMMANDS,
    DB
}

