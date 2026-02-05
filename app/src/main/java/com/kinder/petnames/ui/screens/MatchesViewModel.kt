package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.data.MatchesRepository
import com.kinder.petnames.domain.MatchRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesUiState(
    val matches: List<MatchRow> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val matchesRepository: MatchesRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()
    
    init {
        loadMatches()
    }
    
    private fun loadMatches() {
        viewModelScope.launch {
            val householdId = preferencesManager.householdId.first() ?: return@launch
            
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val matches = matchesRepository.fetchMatches(householdId)
                _uiState.update {
                    it.copy(
                        matches = matches,
                        isLoading = false
                    )
                }
                
                // Track analytics
                analyticsManager.trackMatchesListViewed(matches.size)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }
    
    fun refresh() {
        loadMatches()
    }
}
