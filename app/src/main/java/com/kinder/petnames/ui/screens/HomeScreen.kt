package com.kinder.petnames.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinder.petnames.R
import com.kinder.petnames.ui.components.*
import com.kinder.petnames.ui.theme.PrimaryPurple

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToProfile: () -> Unit,
    onNavigateToLikes: () -> Unit,
    onNavigateToMatches: () -> Unit,
    onNavigateToFilters: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Match popup
    if (uiState.showMatchPopup && uiState.matchedName != null) {
        MatchPopup(
            name = uiState.matchedName!!,
            gender = uiState.matchedGender ?: "any",
            onViewMatches = {
                viewModel.dismissMatchPopup()
                onNavigateToMatches()
            },
            onContinue = {
                viewModel.dismissMatchPopup()
            }
        )
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        // Top bar
        TopBar(
            onProfileClick = onNavigateToProfile,
            onLikesClick = onNavigateToLikes,
            onFiltersClick = onNavigateToFilters,
            onMatchesClick = onNavigateToMatches
        )
        
        // Card stack area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading && uiState.cardStack.isEmpty() -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = PrimaryPurple)
                        Text(
                            text = stringResource(R.string.loading_names),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                uiState.isEmpty || uiState.cardStack.isEmpty() -> {
                    EmptyState(onAdjustFilters = onNavigateToFilters)
                }
                else -> {
                    // Card stack (show up to 3 cards)
                    uiState.cardStack.take(3).reversed().forEachIndexed { reversedIndex, card ->
                        val index = uiState.cardStack.take(3).size - 1 - reversedIndex
                        SwipeableCard(
                            card = card,
                            isTopCard = index == 0,
                            stackIndex = index,
                            onSwipe = { direction ->
                                when (direction) {
                                    SwipeDirection.RIGHT -> viewModel.likeCard()
                                    SwipeDirection.LEFT -> viewModel.dismissCard()
                                }
                            }
                        )
                    }
                }
            }
        }
        
        // Bottom action buttons
        ActionButtonsRow(
            onDislike = { viewModel.dismissCard() },
            onUndo = { viewModel.undoSwipe() },
            onLike = { viewModel.likeCard() },
            canSwipe = uiState.cardStack.isNotEmpty() && !uiState.isAnimating,
            canUndo = uiState.lastSwipe != null && !uiState.isAnimating,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Composable
private fun TopBar(
    onProfileClick: () -> Unit,
    onLikesClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onMatchesClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onProfileClick) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(onClick = onLikesClick) {
            Icon(
                imageVector = Icons.Default.ThumbUp,
                contentDescription = "Likes",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(onClick = onFiltersClick) {
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filters",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(onClick = onMatchesClick) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Matches",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun EmptyState(onAdjustFilters: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(32.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = stringResource(R.string.no_more_names),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = stringResource(R.string.no_more_names_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Button(
            onClick = onAdjustFilters,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
        ) {
            Icon(Icons.Default.Tune, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.adjust_filters))
        }
    }
}
