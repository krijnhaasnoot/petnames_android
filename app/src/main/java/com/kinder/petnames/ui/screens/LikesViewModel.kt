package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.data.SwipesRepository
import com.kinder.petnames.domain.LikedNameRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LikesUiState(
    val likes: List<LikedNameRow> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LikesViewModel @Inject constructor(
    private val swipesRepository: SwipesRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LikesUiState())
    val uiState: StateFlow<LikesUiState> = _uiState.asStateFlow()
    
    init {
        loadLikes()
    }
    
    private fun loadLikes() {
        viewModelScope.launch {
            val householdId = preferencesManager.householdId.first() ?: return@launch
            val userId = preferencesManager.userId.first() ?: return@launch
            
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val likes = swipesRepository.fetchLikes(householdId, userId)
                _uiState.update {
                    it.copy(
                        likes = likes,
                        isLoading = false
                    )
                }
                
                // Track analytics
                analyticsManager.trackLikesViewed(likes.size)
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
}
