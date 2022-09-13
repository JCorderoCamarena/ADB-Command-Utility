@file:OptIn(DelicateCoroutinesApi::class)

package ui.tabs.download.fileList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lordcodes.turtle.ShellLocation
import compose.icons.FeatherIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.RefreshCw
import kotlinx.coroutines.DelicateCoroutinesApi
import utils.StringRes

@Composable
fun PullLogs() {

    val listOfFiles = mutableStateListOf<LogFileItem>()
    var areFileNamesLoading by remember { mutableStateOf(false) }
    val controller by remember { mutableStateOf(FileListController()) }

//    LaunchedEffect(Unit) {
//        getFullFileList(
//            onFileList = {
//                listOfFiles.addAll(it)
//                areFileNamesLoading = false
//            }
//        )
//    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Divider(color = Color.Black, thickness = 4.dp)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = StringRes.selectListOfFilesToDownload
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1F)
            ) {
                FileList(
                    isLoading = areFileNamesLoading,
                    files = listOfFiles,
                    onItemChanged = { index, item ->
                        listOfFiles[index] = item.copy(isChecked = !item.isChecked)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .weight(1F),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(onClick = {
                    listOfFiles.clear()
                    controller.getFullFileList(
                        onFileList = {
                            areFileNamesLoading = false
                            listOfFiles.addAll(it)
                        }
                    )
                }
                ) {
                    Icon(imageVector = FeatherIcons.RefreshCw, contentDescription = null)
                }

                Button(onClick = {
                    println("Download Pressed")
                    areFileNamesLoading = true
                    val root = ShellLocation.HOME.resolve("Desktop")
                    controller.downloadSelectedFiles(root, listOfFiles.filter { it.isChecked }) {
                        areFileNamesLoading = false
                        println("Files downloaded")
                    }
                }) {
                    Icon(imageVector = FeatherIcons.Download, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun FileList(
    isLoading: Boolean,
    files: List<LogFileItem>,
    onItemChanged: (Int, LogFileItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(!isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(files) { index, item ->
                    LabelledCheckbox(
                        checked = item.isChecked,
                        onCheckedChange = {
                            onItemChanged(index, item)
                        },
                        label = item.fileName
                    )
                }

            }
        }
        AnimatedVisibility(isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Blue, strokeWidth = 4.dp)
            }
        }
    }
}

@Composable
fun LabelledCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = colors
        )
        Spacer(Modifier.width(12.dp))
        Text(label)
    }
}
