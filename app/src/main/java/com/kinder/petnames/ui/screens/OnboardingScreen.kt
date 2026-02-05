package com.kinder.petnames.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinder.petnames.R
import com.kinder.petnames.ui.theme.PrimaryPurple
import com.kinder.petnames.ui.theme.PrimaryPurpleDark

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Navigate when onboarded
    LaunchedEffect(uiState.isOnboarded) {
        if (uiState.isOnboarded) {
            onComplete()
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(PrimaryPurple, PrimaryPurpleDark)
                )
            )
            .systemBarsPadding()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo/Title
            Text(
                text = "🐾",
                fontSize = 64.sp
            )
            
            Text(
                text = stringResource(R.string.onboarding_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = stringResource(R.string.onboarding_subtitle),
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Show invite code if created
            if (uiState.createdInviteCode != null) {
                InviteCodeDisplay(
                    code = uiState.createdInviteCode!!,
                    onContinue = { viewModel.completeOnboarding() }
                )
            } else {
                // Name input
                OutlinedTextField(
                    value = uiState.memberName,
                    onValueChange = { viewModel.updateMemberName(it) },
                    label = { Text(stringResource(R.string.enter_name)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Create button
                Button(
                    onClick = { viewModel.createHousehold() },
                    enabled = uiState.memberName.isNotBlank() && !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = PrimaryPurple
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading && uiState.inviteCode.isBlank()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = PrimaryPurple
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.create_household),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                // Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color.White.copy(alpha = 0.3f)
                    )
                    Text(
                        text = stringResource(R.string.or),
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = Color.White.copy(alpha = 0.3f)
                    )
                }
                
                // Join section
                OutlinedTextField(
                    value = uiState.inviteCode,
                    onValueChange = { viewModel.updateInviteCode(it.uppercase()) },
                    label = { Text(stringResource(R.string.enter_invite_code)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Button(
                    onClick = { viewModel.joinHousehold() },
                    enabled = uiState.inviteCode.length >= 6 && 
                             uiState.memberName.isNotBlank() && 
                             !uiState.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.isLoading && uiState.inviteCode.isNotBlank()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.join),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            
            // Error message
            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = Color.Red.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InviteCodeDisplay(
    code: String,
    onContinue: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.share_code),
            color = Color.White.copy(alpha = 0.9f)
        )
        
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.15f))
                .padding(24.dp)
        ) {
            Text(
                text = code,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 4.sp
            )
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { clipboardManager.setText(AnnotatedString(code)) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.copy))
            }
            
            OutlinedButton(
                onClick = { /* TODO: Share intent */ },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.share))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = PrimaryPurple
            )
        ) {
            Text(
                text = stringResource(R.string.continue_to_app),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
