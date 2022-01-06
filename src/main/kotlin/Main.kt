// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.inputText.ShellInputTextField
import ui.localDatabase.PullLocalDatabase
import ui.localDatabase.PushLocalDatabase
import utils.StringRes

@Composable
@Preview
fun App() {

    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            PullLocalDatabase()
            PushLocalDatabase()
            ShellInputTextField()
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

