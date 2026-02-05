package com.kinder.petnames.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.kinder.petnames.R
import com.kinder.petnames.ui.theme.*

@Composable
fun MatchPopup(
    name: String,
    gender: String,
    onViewMatches: () -> Unit,
    onContinue: () -> Unit
) {
    val gradientColors = when (gender.lowercase()) {
        "female" -> listOf(FemaleColor, FemaleColor.copy(alpha = 0.8f))
        "male" -> listOf(MaleColor, MaleColor.copy(alpha = 0.8f))
        "neutral" -> listOf(NeutralColor, NeutralColor.copy(alpha = 0.8f))
        else -> listOf(LikeGreen, LikeGreenLight)
    }
    
    Dialog(onDismissRequest = onContinue) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Brush.linearGradient(gradientColors))
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Match icon
                Text(
                    text = "💕",
                    fontSize = 64.sp
                )
                
                // Title
                Text(
                    text = stringResource(R.string.its_a_match),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                // Description
                Text(
                    text = stringResource(R.string.match_description, name),
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
                
                // Name display
                Text(
                    text = name,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Buttons
                Button(
                    onClick = onViewMatches,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = gradientColors.first()
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.view_matches),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                TextButton(
                    onClick = onContinue,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.keep_swiping))
                }
            }
        }
    }
}
