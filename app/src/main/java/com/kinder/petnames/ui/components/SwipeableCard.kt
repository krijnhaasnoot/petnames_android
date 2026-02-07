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
    
    // Direct state for dragging - NO animation during drag
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    var isAnimatingOut by remember { mutableStateOf(false) }
    
    // Calculate rotation from offset
    val rotation = offsetX * AppConfig.CARD_ROTATION_MULTIPLIER
    
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
    val swipeIntensity = (abs(offsetX) / 150f).coerceIn(0f, 1f)
    
    // Swipe direction color overlay
    val swipeOverlayColor = when {
        offsetX > 30 -> LikeGreen.copy(alpha = swipeIntensity * 0.4f)
        offsetX < -30 -> DismissRed.copy(alpha = swipeIntensity * 0.4f)
        else -> Color.Transparent
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .graphicsLayer {
                // Direct values - no animation wrapper
                translationX = offsetX
                translationY = offsetY + yOffset
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (isTopCard && !isAnimatingOut) {
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
                                val velocityThreshold = 800f
                                
                                val shouldSwipeRight = offsetX > threshold || 
                                    (offsetX > 50 && velocity.x > velocityThreshold)
                                val shouldSwipeLeft = offsetX < -threshold || 
                                    (offsetX < -50 && velocity.x < -velocityThreshold)
                                
                                when {
                                    shouldSwipeRight -> {
                                        isAnimatingOut = true
                                        scope.launch {
                                            // Quick fly-off animation
                                            val targetX = screenWidthPx * 1.5f
                                            val startX = offsetX
                                            val startRotation = rotation
                                            val targetRotation = 20f
                                            
                                            // Animate over ~150ms
                                            val duration = 150
                                            val startTime = System.currentTimeMillis()
                                            
                                            while (System.currentTimeMillis() - startTime < duration) {
                                                val elapsed = (System.currentTimeMillis() - startTime).toFloat()
                                                val progress = (elapsed / duration).coerceIn(0f, 1f)
                                                // Ease out quad
                                                val eased = 1f - (1f - progress) * (1f - progress)
                                                
                                                offsetX = startX + (targetX - startX) * eased
                                                // rotation is calculated from offsetX
                                                
                                                kotlinx.coroutines.delay(8) // ~120fps
                                            }
                                            
                                            offsetX = targetX
                                            onSwipe(SwipeDirection.RIGHT)
                                        }
                                    }
                                    shouldSwipeLeft -> {
                                        isAnimatingOut = true
                                        scope.launch {
                                            val targetX = -screenWidthPx * 1.5f
                                            val startX = offsetX
                                            
                                            val duration = 150
                                            val startTime = System.currentTimeMillis()
                                            
                                            while (System.currentTimeMillis() - startTime < duration) {
                                                val elapsed = (System.currentTimeMillis() - startTime).toFloat()
                                                val progress = (elapsed / duration).coerceIn(0f, 1f)
                                                val eased = 1f - (1f - progress) * (1f - progress)
                                                
                                                offsetX = startX + (targetX - startX) * eased
                                                
                                                kotlinx.coroutines.delay(8)
                                            }
                                            
                                            offsetX = targetX
                                            onSwipe(SwipeDirection.LEFT)
                                        }
                                    }
                                    else -> {
                                        // Spring back to center
                                        scope.launch {
                                            val startX = offsetX
                                            val startY = offsetY
                                            
                                            // Spring animation ~300ms
                                            val duration = 300
                                            val startTime = System.currentTimeMillis()
                                            
                                            while (System.currentTimeMillis() - startTime < duration) {
                                                val elapsed = (System.currentTimeMillis() - startTime).toFloat()
                                                val progress = (elapsed / duration).coerceIn(0f, 1f)
                                                
                                                // Spring-like ease out with overshoot
                                                val eased = if (progress < 0.5f) {
                                                    2f * progress * progress
                                                } else {
                                                    val t = progress - 0.5f
                                                    0.5f + 2f * t * (1f - t) + 0.5f * (1f - (1f - 2f * t) * (1f - 2f * t))
                                                }.coerceIn(0f, 1f)
                                                
                                                offsetX = startX * (1f - eased)
                                                offsetY = startY * (1f - eased)
                                                
                                                kotlinx.coroutines.delay(8)
                                            }
                                            
                                            offsetX = 0f
                                            offsetY = 0f
                                        }
                                    }
                                }
                            },
                            onDragCancel = {
                                isDragging = false
                                offsetX = 0f
                                offsetY = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )
                                
                                // DIRECT update - no animation, instant tracking
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y * 0.3f
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
                    ambientColor = if (offsetX > 30) LikeGreen.copy(alpha = 0.3f)
                                  else if (offsetX < -30) DismissRed.copy(alpha = 0.3f)
                                  else Color.Black.copy(alpha = 0.2f)
                )
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(gradientColors))
                .background(swipeOverlayColor),
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
                if (offsetX > 30) {
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
                if (offsetX < -30) {
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
