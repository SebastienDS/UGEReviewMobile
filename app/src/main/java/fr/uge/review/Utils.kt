package fr.uge.review

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Icon(imageVector: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        androidx.compose.material3.Icon(imageVector = imageVector, contentDescription = null)
    }
}