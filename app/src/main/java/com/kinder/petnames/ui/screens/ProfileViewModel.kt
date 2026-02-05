package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.data.HouseholdRepository
import com.kinder.petnames.data.SwipesRepository
import com.kinder.petnames.domain.HouseholdMember
import com.kinder.petnames.domain.SwipeCounts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val inviteCode: String? = null,
    val members: List<HouseholdMember> = emptyList(),
    val counts: SwipeCounts? = null,
    val currentUserId: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val swipesRepository: SwipesRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadProfileData()
        analyticsManager.trackProfileViewed()
    }
    
    private fun loadProfileData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            val householdId = preferencesManager.householdId.first() ?: return@launch
            val userId = preferencesManager.userId.first()
            val inviteCode = preferencesManager.inviteCode.first()
            
            _uiState.update { it.copy(currentUserId = userId, inviteCode = inviteCode) }
            
            // Load invite code if not cached
            if (inviteCode == null) {
                try {
                    val code = householdRepository.fetchInviteCode(householdId)
                    if (code != null) {
                        preferencesManager.setInviteCode(code)
                        _uiState.update { it.copy(inviteCode = code) }
                    }
                } catch (e: Exception) {
                    println("Failed to fetch invite code: ${e.message}")
                }
            }
            
            // Load members
            try {
                val members = householdRepository.fetchHouseholdMembers(householdId)
                _uiState.update { it.copy(members = members) }
            } catch (e: Exception) {
                println("Failed to fetch members: ${e.message}")
            }
            
            // Load counts
            if (userId != null) {
                try {
                    val counts = swipesRepository.fetchCounts(householdId, userId)
                    _uiState.update { it.copy(counts = counts) }
                } catch (e: Exception) {
                    println("Failed to fetch counts: ${e.message}")
                }
            }
            
            _uiState.update { it.copy(isLoading = false) }
        }
    }
    
    fun resetHousehold() {
        viewModelScope.launch {
            preferencesManager.clearHousehold()
            preferencesManager.clearSwipedNames()
        }
    }
}
