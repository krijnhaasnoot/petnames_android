package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.domain.Filters
import com.kinder.petnames.domain.PetLanguage
import com.kinder.petnames.domain.PetStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FiltersUiState(
    val filters: Filters = Filters.DEFAULT,
    val selectedLanguages: Set<PetLanguage> = setOf(PetLanguage.NL, PetLanguage.EN),
    val viewingLanguage: PetLanguage = PetLanguage.EN,
    val enabledStyles: Set<PetStyle> = PetStyle.entries.toSet()
)

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()
    
    init {
        loadFilters()
    }
    
    private fun loadFilters() {
        viewModelScope.launch {
            val filters = preferencesManager.filters.first()
            val languages = preferencesManager.selectedLanguages.first()
            val viewingLang = preferencesManager.viewingLanguage.first()
            val styles = preferencesManager.enabledStyles.first()
            
            _uiState.update {
                it.copy(
                    filters = filters,
                    selectedLanguages = languages,
                    viewingLanguage = viewingLang,
                    enabledStyles = styles
                )
            }
        }
    }
    
    fun toggleLanguage(language: PetLanguage) {
        val current = _uiState.value.selectedLanguages
        
        // Don't allow disabling the last language
        if (current.contains(language) && current.size <= 1) return
        
        val newSet = if (current.contains(language)) {
            current - language
        } else {
            current + language
        }
        
        _uiState.update { it.copy(selectedLanguages = newSet) }
        
        // Track analytics
        analyticsManager.trackLanguagesSelected(newSet.map { it.code })
    }
    
    fun toggleStyle(style: PetStyle) {
        val current = _uiState.value.enabledStyles
        val newSet = if (current.contains(style)) {
            current - style
        } else {
            current + style
        }
        
        _uiState.update { it.copy(enabledStyles = newSet) }
        
        // Track analytics
        analyticsManager.trackStylesEnabled(newSet.map { it.code })
    }
    
    fun updateGender(gender: String) {
        _uiState.update {
            it.copy(filters = it.filters.copy(gender = gender))
        }
        analyticsManager.trackFilterChanged("gender", gender)
    }
    
    fun updateStartsWith(letter: String) {
        _uiState.update {
            it.copy(filters = it.filters.copy(startsWith = letter))
        }
        analyticsManager.trackFilterChanged("starts_with", letter)
    }
    
    fun updateMaxLength(length: Int) {
        _uiState.update {
            it.copy(filters = it.filters.copy(maxLength = length))
        }
        analyticsManager.trackFilterChanged("max_length", length.toString())
    }
    
    fun resetFilters() {
        _uiState.update {
            FiltersUiState()
        }
    }
    
    fun applyFilters() {
        viewModelScope.launch {
            val state = _uiState.value
            preferencesManager.setFilters(state.filters)
            preferencesManager.setSelectedLanguages(state.selectedLanguages)
            preferencesManager.setViewingLanguage(state.viewingLanguage)
            preferencesManager.setEnabledStyles(state.enabledStyles)
        }
    }
}
