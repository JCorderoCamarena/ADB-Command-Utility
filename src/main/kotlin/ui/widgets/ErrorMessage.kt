package ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorMessage(error: String) {
    AnimatedVisibility(visible = error.isNotEmpty()) {
        Text(
            text = error,
            color = MaterialTheme.colors.error
        )
    }
}