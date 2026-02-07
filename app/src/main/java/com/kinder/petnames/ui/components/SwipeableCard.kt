package com.kinder.petnames.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kinder.petnames.core.AppConfig
import com.kinder.petnames.domain.NameCard
import com.kinder.petnames.ui.theme.*
import kotlinx.coroutines.launch

enum class SwipeDirection {
    LEFT, RIGHT
}

@Composable
fun SwipeableCard(
    card: NameCard,
    isTopCard: Boolean,
    stackIndex: Int,
    onSwipe: (SwipeDirection) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    
    // Stack visual effects
    val scale = 1f - (stackIndex * 0.04f)
    val yOffset = stackIndex * 12f
    
    // Gender-based gradient colors (matching iOS)
    val gradientColors = when (card.gender.lowercase()) {
        "female" -> listOf(Color(0xFFFF2C55), Color(0xFFFF5C7A))  // Pink
        "male" -> listOf(Color(0xFF1491F4), Color(0xFF4DB3FF))    // Blue
        "neutral" -> listOf(Color(0xFF2CB3B0), Color(0xFF5DD4D1)) // Teal
        else -> listOf(Color(0xFF11998E), Color(0xFF38EF7D))      // Green
    }
    
    val genderSymbol = when (card.gender.lowercase()) {
        "female" -> "♀"
        "male" -> "♂"
        "neutral" -> "⚥"
        else -> ""
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value + yOffset
                rotationZ = rotation.value
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isTopCard) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                val threshold = with(density) { AppConfig.SWIPE_THRESHOLD.dp.toPx() }
                                scope.launch {
                                    when {
                                        offsetX.value > threshold -> {
                                            // Animate off screen right
                                            offsetX.animateTo(
                                                with(density) { screenWidth.toPx() * 1.5f },
                                                spring(stiffness = Spring.StiffnessLow)
                                            )
                                            onSwipe(SwipeDirection.RIGHT)
                                        }
                                        offsetX.value < -threshold -> {
                                            // Animate off screen left
                                            offsetX.animateTo(
                                                with(density) { -screenWidth.toPx() * 1.5f },
                                                spring(stiffness = Spring.StiffnessLow)
                                            )
                                            onSwipe(SwipeDirection.LEFT)
                                        }
                                        else -> {
                                            // Return to center
                                            launch { offsetX.animateTo(0f, spring()) }
                                            launch { offsetY.animateTo(0f, spring()) }
                                            launch { rotation.animateTo(0f, spring()) }
                                        }
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch {
                                    offsetX.snapTo(offsetX.value + dragAmount.x)
                                    offsetY.snapTo(offsetY.value + dragAmount.y)
                                    rotation.snapTo(offsetX.value * AppConfig.CARD_ROTATION_MULTIPLIER)
                                }
                            }
                        )
                    }
                } else Modifier
            )
    ) {
        // Card content - SQUARE aspect ratio (1:1)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Square!
                .shadow(16.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(gradientColors)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                // Gender symbol
                Text(
                    text = genderSymbol,
                    fontSize = 28.sp,
                    fontFamily = Poppins,
                    color = Color.White.copy(alpha = 0.9f)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Name - Poppins Bold
                Text(
                    text = card.name,
                    fontSize = 44.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Set title
                Text(
                    text = card.setTitle,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
            
            // Swipe indicators
            if (isTopCard) {
                // Like indicator (right)
                if (offsetX.value > 20) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(20.dp)
                            .background(
                                LikeGreen.copy(alpha = (offsetX.value / 200f).coerceIn(0f, 1f)),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "LIKE",
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
                
                // Nope indicator (left)
                if (offsetX.value < -20) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .background(
                                DismissRed.copy(alpha = (-offsetX.value / 200f).coerceIn(0f, 1f)),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "NOPE",
                            color = Color.White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Programmatically trigger a swipe animation
 */
suspend fun animateSwipe(
    offsetX: Animatable<Float, *>,
    rotation: Animatable<Float, *>,
    direction: SwipeDirection,
    screenWidthPx: Float
) {
    val targetX = if (direction == SwipeDirection.RIGHT) screenWidthPx * 1.5f else -screenWidthPx * 1.5f
    val targetRotation = if (direction == SwipeDirection.RIGHT) 20f else -20f
    
    kotlinx.coroutines.coroutineScope {
        launch { offsetX.animateTo(targetX, spring(stiffness = Spring.StiffnessLow)) }
        launch { rotation.animateTo(targetRotation, spring(stiffness = Spring.StiffnessLow)) }
    }
}
