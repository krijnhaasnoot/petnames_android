package com.kinder.petnames.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kinder.petnames.ui.theme.*

enum class ActionButtonType {
    LIKE, DISLIKE, UNDO
}

@Composable
fun ActionButton(
    type: ActionButtonType,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val (icon, backgroundColor, iconColor, size) = when (type) {
        ActionButtonType.LIKE -> ActionButtonStyle(
            icon = Icons.Default.Favorite,
            backgroundColor = LikeGreen,
            iconColor = Color.White,
            size = 64.dp
        )
        ActionButtonType.DISLIKE -> ActionButtonStyle(
            icon = Icons.Default.Close,
            backgroundColor = DismissRed,
            iconColor = Color.White,
            size = 64.dp
        )
        ActionButtonType.UNDO -> ActionButtonStyle(
            icon = Icons.Default.Refresh,
            backgroundColor = UndoYellow,
            iconColor = Color.White,
            size = 48.dp
        )
    }
    
    Box(
        modifier = modifier
            .size(size)
            .shadow(8.dp, CircleShape)
            .clip(CircleShape)
            .background(
                if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.4f)
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = type.name,
            tint = if (enabled) iconColor else iconColor.copy(alpha = 0.5f),
            modifier = Modifier.size(size * 0.5f)
        )
    }
}

private data class ActionButtonStyle(
    val icon: ImageVector,
    val backgroundColor: Color,
    val iconColor: Color,
    val size: Dp
)

@Composable
fun ActionButtonsRow(
    onDislike: () -> Unit,
    onUndo: () -> Unit,
    onLike: () -> Unit,
    canSwipe: Boolean,
    canUndo: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActionButton(
            type = ActionButtonType.DISLIKE,
            onClick = onDislike,
            enabled = canSwipe
        )
        
        ActionButton(
            type = ActionButtonType.UNDO,
            onClick = onUndo,
            enabled = canUndo
        )
        
        ActionButton(
            type = ActionButtonType.LIKE,
            onClick = onLike,
            enabled = canSwipe
        )
    }
}
