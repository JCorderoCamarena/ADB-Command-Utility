// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.lordcodes.turtle.ShellFailedException
import com.lordcodes.turtle.ShellLocation
import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.shellRun
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs
import java.io.File

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Column {
            OpenLocalDatabaseButton()
        }
    }
}

fun main() = application {
    Window(title = "ADB Command Utility", onCloseRequest = ::exitApplication) {
        App()
    }
}


@Composable
fun OpenLocalDatabaseButton() {
    var errorMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        Button(onClick = {
            errorMessage = ""
            when(hostOs) {
                OS.Windows -> {
                    TODO()
                }
                OS.MacOS -> {
                    val root = ShellLocation.HOME.resolve("Desktop")
                    try {
                        pullLocalDatabase(root)
                    } catch (e: ShellRunException) {
                        errorMessage = e.errorText
                    }
                }
                else -> Unit
            }
        }) {
            Text("Pull Local Database")
        }

        AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error
            )
        }

    }
}

fun pullLocalDatabase(root: File) {
    try {
        shellRun(root) {
            files.openFile(path = "LocalDatabase.db")
        }
    } catch (e: ShellRunException) {
        throw e
    }
}
