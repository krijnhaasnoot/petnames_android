package com.kinder.petnames.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
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
import androidx.compose.ui.input.pointer.util.VelocityTracker
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
import kotlin.math.abs

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
    val screenWidthDp = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidthDp.toPx() }
    
    // Animation states - use Float for smoother animations
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    
    // Animated values for smooth transitions
    val animatedOffsetX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = if (isDragging) {
            snap() // Instant while dragging
        } else {
            spring(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "offsetX"
    )
    
    val animatedOffsetY by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = if (isDragging) {
            snap()
        } else {
            spring(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "offsetY"
    )
    
    val animatedRotation by animateFloatAsState(
        targetValue = rotation,
        animationSpec = if (isDragging) {
            snap()
        } else {
            spring(
                dampingRatio = 0.7f,
                stiffness = Spring.StiffnessMedium
            )
        },
        label = "rotation"
    )
    
    val scope = rememberCoroutineScope()
    
    // Stack visual effects
    val scale = 1f - (stackIndex * 0.04f)
    val yOffset = stackIndex * 12f
    
    // Gender-based gradient colors
    val gradientColors = when (card.gender.lowercase()) {
        "female" -> listOf(Color(0xFFFF6B7A), Color(0xFFFF8A96))
        "male" -> listOf(Color(0xFF4A90E2), Color(0xFF6BA8F0))
        "neutral" -> listOf(Color(0xFF2DBAA6), Color(0xFF4DD4C0))
        else -> listOf(Color(0xFF11998E), Color(0xFF38EF7D))
    }
    
    val genderSymbol = when (card.gender.lowercase()) {
        "female" -> "♀"
        "male" -> "♂"
        "neutral" -> "⚥"
        else -> ""
    }
    
    // Swipe intensity for visual feedback (0 to 1)
    val swipeIntensity = (abs(animatedOffsetX) / 150f).coerceIn(0f, 1f)
    
    // Swipe direction color overlay
    val swipeOverlayColor = when {
        animatedOffsetX > 30 -> LikeGreen.copy(alpha = swipeIntensity * 0.4f)
        animatedOffsetX < -30 -> DismissRed.copy(alpha = swipeIntensity * 0.4f)
        else -> Color.Transparent
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .graphicsLayer {
                translationX = animatedOffsetX
                translationY = animatedOffsetY + yOffset
                rotationZ = animatedRotation
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isTopCard) {
                    Modifier.pointerInput(card.id) {
                        val velocityTracker = VelocityTracker()
                        
                        detectDragGestures(
                            onDragStart = {
                                isDragging = true
                                velocityTracker.resetTracking()
                            },
                            onDragEnd = {
                                isDragging = false
                                val velocity = velocityTracker.calculateVelocity()
                                val threshold = AppConfig.SWIPE_THRESHOLD * density.density
                                val velocityThreshold = 800f // px/s
                                
                                // Check if swipe threshold reached OR velocity is high enough
                                val shouldSwipeRight = offsetX > threshold || 
                                    (offsetX > 50 && velocity.x > velocityThreshold)
                                val shouldSwipeLeft = offsetX < -threshold || 
                                    (offsetX < -50 && velocity.x < -velocityThreshold)
                                
                                when {
                                    shouldSwipeRight -> {
                                        // Animate off screen right - fast!
                                        scope.launch {
                                            val targetX = screenWidthPx * 1.5f
                                            val steps = 8
                                            val stepDelay = 15L // ~120fps feel
                                            
                                            for (i in 1..steps) {
                                                val progress = i.toFloat() / steps
                                                val eased = 1f - (1f - progress) * (1f - progress) // easeOut
                                                offsetX = offsetX + (targetX - offsetX) * eased * 0.3f
                                                rotation = rotation + (20f - rotation) * eased * 0.3f
                                                kotlinx.coroutines.delay(stepDelay)
                                            }
                                            onSwipe(SwipeDirection.RIGHT)
                                        }
                                    }
                                    shouldSwipeLeft -> {
                                        // Animate off screen left - fast!
                                        scope.launch {
                                            val targetX = -screenWidthPx * 1.5f
                                            val steps = 8
                                            val stepDelay = 15L
                                            
                                            for (i in 1..steps) {
                                                val progress = i.toFloat() / steps
                                                val eased = 1f - (1f - progress) * (1f - progress)
                                                offsetX = offsetX + (targetX - offsetX) * eased * 0.3f
                                                rotation = rotation + (-20f - rotation) * eased * 0.3f
                                                kotlinx.coroutines.delay(stepDelay)
                                            }
                                            onSwipe(SwipeDirection.LEFT)
                                        }
                                    }
                                    else -> {
                                        // Return to center with spring
                                        offsetX = 0f
                                        offsetY = 0f
                                        rotation = 0f
                                    }
                                }
                            },
                            onDragCancel = {
                                isDragging = false
                                offsetX = 0f
                                offsetY = 0f
                                rotation = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )
                                
                                // Update position immediately
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y * 0.3f // Reduced vertical movement
                                rotation = offsetX * AppConfig.CARD_ROTATION_MULTIPLIER
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
                .aspectRatio(1f)
                .shadow(
                    elevation = if (swipeIntensity > 0.3f) 24.dp else 16.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = if (animatedOffsetX > 30) LikeGreen.copy(alpha = 0.3f)
                                  else if (animatedOffsetX < -30) DismissRed.copy(alpha = 0.3f)
                                  else Color.Black.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(gradientColors))
                .background(swipeOverlayColor), // Swipe color overlay
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
                
                // Set title in capsule
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.25f))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = card.setTitle,
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Swipe indicators - circular like iOS
            if (isTopCard) {
                // Like indicator (left side when swiping right)
                if (animatedOffsetX > 30) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(20.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                alpha = swipeIntensity
                                scaleX = 0.8f + swipeIntensity * 0.4f
                                scaleY = 0.8f + swipeIntensity * 0.4f
                                rotationZ = -15f
                            }
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Like",
                            tint = LikeGreen,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                // Nope indicator (right side when swiping left)
                if (animatedOffsetX < -30) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .size(60.dp)
                            .graphicsLayer {
                                alpha = swipeIntensity
                                scaleX = 0.8f + swipeIntensity * 0.4f
                                scaleY = 0.8f + swipeIntensity * 0.4f
                                rotationZ = 15f
                            }
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Nope",
                            tint = DismissRed,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}
