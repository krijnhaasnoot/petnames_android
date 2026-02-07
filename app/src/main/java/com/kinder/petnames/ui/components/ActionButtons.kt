package com.kinder.petnames.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
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
    var isPressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(
            dampingRatio = 0.5f,
            stiffness = 400f
        ),
        label = "buttonScale"
    )
    
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
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (isPressed) 4.dp else 8.dp,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(
                if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.4f)
            )
            .pointerInput(enabled) {
                if (enabled) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        },
                        onTap = {
                            onClick()
                        }
                    )
                }
            },
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
