package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.core.SessionManager
import com.kinder.petnames.data.HouseholdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val memberName: String = "",
    val inviteCode: String = "",
    val createdInviteCode: String? = null,
    val isLoading: Boolean = false,
    val isOnboarded: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val householdRepository: HouseholdRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()
    
    init {
        // Check if already onboarded
        viewModelScope.launch {
            preferencesManager.householdId.collect { id ->
                if (id != null) {
                    _uiState.update { it.copy(isOnboarded = true) }
                }
            }
        }
    }
    
    fun updateMemberName(name: String) {
        _uiState.update { it.copy(memberName = name, errorMessage = null) }
    }
    
    fun updateInviteCode(code: String) {
        _uiState.update { it.copy(inviteCode = code.take(8), errorMessage = null) }
    }
    
    fun createHousehold() {
        val name = _uiState.value.memberName.trim()
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // Sign in anonymously first
                sessionManager.signInAnonymouslyIfNeeded()
                
                val response = householdRepository.createHousehold(name.ifBlank { null } ?: "")
                
                // Save to preferences
                preferencesManager.setHouseholdId(response.householdId)
                preferencesManager.setInviteCode(response.inviteCode)
                
                // Track analytics
                analyticsManager.trackHouseholdCreated()
                analyticsManager.trackOnboardingCompleted("create")
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        createdInviteCode = response.inviteCode
                    )
                }
            } catch (e: Exception) {
                println("❌ Create household error: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to create household"
                    )
                }
            }
        }
    }
    
    fun joinHousehold() {
        val name = _uiState.value.memberName.trim()
        val code = _uiState.value.inviteCode.trim()
        if (code.length < 6) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            try {
                // Sign in anonymously first
                sessionManager.signInAnonymouslyIfNeeded()
                
                val response = householdRepository.joinHousehold(code, name)
                
                // Save to preferences
                preferencesManager.setHouseholdId(response.householdId)
                preferencesManager.setInviteCode(code)
                
                // Track analytics
                analyticsManager.trackHouseholdJoined()
                analyticsManager.trackOnboardingCompleted("join")
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isOnboarded = true
                    )
                }
            } catch (e: Exception) {
                println("❌ Join household error: ${e.message}")
                e.printStackTrace()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Invite code niet gevonden. Controleer en probeer opnieuw."
                    )
                }
            }
        }
    }
    
    fun completeOnboarding() {
        _uiState.update { it.copy(isOnboarded = true) }
    }
}
