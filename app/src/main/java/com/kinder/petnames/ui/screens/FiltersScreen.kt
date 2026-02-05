package com.kinder.petnames.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kinder.petnames.R
import com.kinder.petnames.domain.Filters
import com.kinder.petnames.domain.PetLanguage
import com.kinder.petnames.domain.PetStyle
import com.kinder.petnames.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersScreen(
    viewModel: FiltersViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onApply: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.filters)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    TextButton(onClick = { viewModel.resetFilters() }) {
                        Text(stringResource(R.string.reset))
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    viewModel.applyFilters()
                    onApply()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple)
            ) {
                Text(stringResource(R.string.apply), fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Languages Section
            item {
                SectionHeader(
                    title = stringResource(R.string.languages),
                    icon = Icons.Default.Language
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PetLanguage.entries) { language ->
                        LanguageChip(
                            language = language,
                            isSelected = uiState.selectedLanguages.contains(language),
                            onClick = { viewModel.toggleLanguage(language) }
                        )
                    }
                }
            }
            
            // Styles Section
            item {
                SectionHeader(
                    title = stringResource(R.string.styles),
                    icon = Icons.Default.Style
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    PetStyle.entries.forEach { style ->
                        StyleRow(
                            style = style,
                            language = uiState.viewingLanguage,
                            isEnabled = uiState.enabledStyles.contains(style),
                            onClick = { viewModel.toggleStyle(style) }
                        )
                    }
                }
            }
            
            // Gender Filter
            item {
                SectionHeader(
                    title = stringResource(R.string.gender),
                    icon = Icons.Default.Person
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Filters.GENDER_OPTIONS.forEach { gender ->
                        FilterChip(
                            selected = uiState.filters.gender == gender,
                            onClick = { viewModel.updateGender(gender) },
                            label = {
                                Text(
                                    when (gender) {
                                        "any" -> stringResource(R.string.any)
                                        "male" -> stringResource(R.string.male)
                                        "female" -> stringResource(R.string.female)
                                        "neutral" -> stringResource(R.string.neutral)
                                        else -> gender
                                    }
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            
            // Starts With Filter
            item {
                SectionHeader(
                    title = stringResource(R.string.starts_with),
                    icon = Icons.Default.TextFields
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(Filters.STARTS_WITH_OPTIONS) { letter ->
                        LetterChip(
                            letter = letter,
                            isSelected = uiState.filters.startsWith == letter,
                            onClick = { viewModel.updateStartsWith(letter) }
                        )
                    }
                }
            }
            
            // Max Length Filter
            item {
                SectionHeader(
                    title = stringResource(R.string.max_length),
                    icon = Icons.Default.Straighten
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (uiState.filters.maxLength == 0) stringResource(R.string.any)
                               else "${uiState.filters.maxLength} letters",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Slider(
                        value = uiState.filters.maxLength.toFloat(),
                        onValueChange = { viewModel.updateMaxLength(it.toInt()) },
                        valueRange = 0f..15f,
                        steps = 14,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = PrimaryPurple,
                            activeTrackColor = PrimaryPurple
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryPurple,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun LanguageChip(
    language: PetLanguage,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) PrimaryPurple else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = language.flag, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = language.shortName,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun StyleRow(
    style: PetStyle,
    language: PetLanguage,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val styleColor = Color(android.graphics.Color.parseColor("#${style.colorHex}"))
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isEnabled) styleColor.copy(alpha = 0.1f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                width = if (isEnabled) 2.dp else 0.dp,
                color = if (isEnabled) styleColor else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = style.icon, fontSize = 24.sp)
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = style.title(language),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isEnabled) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        
        if (isEnabled) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = styleColor
            )
        }
    }
}

@Composable
private fun LetterChip(
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) PrimaryPurple else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (letter == "any") "∀" else letter.uppercase(),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }
}
