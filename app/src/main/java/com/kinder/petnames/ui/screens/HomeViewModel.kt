package com.kinder.petnames.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinder.petnames.core.AnalyticsManager
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.core.SessionManager
import com.kinder.petnames.data.NamesRepository
import com.kinder.petnames.data.SwipesRepository
import com.kinder.petnames.domain.NameCard
import com.kinder.petnames.domain.SwipeDecision
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val cardStack: List<NameCard> = emptyList(),
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val isAnimating: Boolean = false,
    val lastSwipe: LastSwipe? = null,
    val showMatchPopup: Boolean = false,
    val matchedName: String? = null,
    val matchedGender: String? = null,
    val errorMessage: String? = null
)

data class LastSwipe(
    val card: NameCard,
    val decision: SwipeDecision
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val namesRepository: NamesRepository,
    private val swipesRepository: SwipesRepository,
    private val preferencesManager: PreferencesManager,
    private val analyticsManager: AnalyticsManager,
    private val sessionManager: SessionManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    private var householdId: String? = null
    private var userId: String? = null
    private var hasLoadedInitial = false
    
    init {
        viewModelScope.launch {
            // Load user data
            launch {
                preferencesManager.householdId.collect { id ->
                    householdId = id
                    if (id != null && !hasLoadedInitial) {
                        hasLoadedInitial = true
                        loadAllNames()
                    }
                }
            }
            launch {
                preferencesManager.userId.collect { id ->
                    userId = id
                }
            }
            
            // Watch for filter changes and reload names
            launch {
                // Combine all filter-related flows
                combine(
                    preferencesManager.filters,
                    preferencesManager.selectedLanguages,
                    preferencesManager.enabledStyles
                ) { filters, languages, styles ->
                    Triple(filters, languages, styles)
                }.collect {
                    // Only reload if we've already loaded initial data and have householdId
                    if (hasLoadedInitial && householdId != null) {
                        println("🔄 Filters changed, reloading names...")
                        loadAllNames()
                    }
                }
            }
        }
    }
    
    fun loadAllNames() {
        val hId = householdId ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            try {
                val filters = preferencesManager.filters.first()
                val enabledStyles = preferencesManager.enabledStyles.first()
                val selectedLanguages = preferencesManager.selectedLanguages.first()
                
                // Build enabled set IDs from languages and styles
                val enabledSetIds = selectedLanguages.flatMap { lang ->
                    enabledStyles.map { style ->
                        "pets_${lang.code}_${style.code}"
                    }
                }
                
                val cards = namesRepository.getNextNames(
                    householdId = hId,
                    enabledSetIds = enabledSetIds,
                    gender = filters.gender,
                    startsWith = filters.startsWith,
                    maxLength = filters.maxLength
                )
                
                _uiState.update {
                    it.copy(
                        cardStack = cards,
                        isLoading = false,
                        isEmpty = cards.isEmpty()
                    )
                }
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
    
    fun likeCard() {
        val card = _uiState.value.cardStack.firstOrNull() ?: return
        val hId = householdId ?: run {
            println("❌ likeCard: No householdId")
            return
        }
        val uId = userId ?: sessionManager.currentUserId.value ?: run {
            println("❌ likeCard: No userId")
            return
        }
        println("✅ likeCard: ${card.name}, hId=$hId, uId=$uId")
        
        // Save last swipe for undo
        _uiState.update {
            it.copy(
                lastSwipe = LastSwipe(card, SwipeDecision.LIKE),
                cardStack = it.cardStack.drop(1),
                isEmpty = it.cardStack.size <= 1
            )
        }
        
        // Track analytics
        analyticsManager.trackNameSwiped(
            decision = "like",
            name = card.name,
            gender = card.gender
        )
        
        // Mark as swiped locally and save like
        viewModelScope.launch {
            namesRepository.markNameAsSwiped(card.name)
            
            // Save like locally (offline-first)
            preferencesManager.addLocalLike(
                name = card.name,
                gender = card.gender,
                setTitle = card.setTitle
            )
            
            // Sync to server
            try {
                val isMatch = if (card.isLocal) {
                    swipesRepository.insertSwipeByName(
                        householdId = hId,
                        userId = uId,
                        name = card.name,
                        gender = card.gender,
                        decision = SwipeDecision.LIKE
                    )
                } else {
                    swipesRepository.insertSwipe(
                        householdId = hId,
                        userId = uId,
                        nameId = card.id,
                        decision = SwipeDecision.LIKE
                    )
                    swipesRepository.checkForMatchByName(hId, card.name, uId)
                }
                
                if (isMatch) {
                    _uiState.update {
                        it.copy(
                            showMatchPopup = true,
                            matchedName = card.name,
                            matchedGender = card.gender
                        )
                    }
                    analyticsManager.trackMatchCreated(card.name, card.gender)
                }
            } catch (e: Exception) {
                // Silently fail - swipe is tracked locally
                println("⚠️ Failed to sync like: ${e.message}")
            }
        }
    }
    
    fun dismissCard() {
        val card = _uiState.value.cardStack.firstOrNull() ?: return
        val hId = householdId ?: run {
            println("❌ dismissCard: No householdId")
            return
        }
        val uId = userId ?: sessionManager.currentUserId.value ?: run {
            println("❌ dismissCard: No userId")
            return
        }
        println("✅ dismissCard: ${card.name}, hId=$hId, uId=$uId")
        
        // Save last swipe for undo
        _uiState.update {
            it.copy(
                lastSwipe = LastSwipe(card, SwipeDecision.DISMISS),
                cardStack = it.cardStack.drop(1),
                isEmpty = it.cardStack.size <= 1
            )
        }
        
        // Track analytics
        analyticsManager.trackNameSwiped(
            decision = "dismiss",
            name = card.name,
            gender = card.gender
        )
        
        // Mark as swiped locally
        viewModelScope.launch {
            namesRepository.markNameAsSwiped(card.name)
            
            // Sync to server (only for non-local names)
            if (!card.isLocal) {
                try {
                    swipesRepository.insertSwipe(
                        householdId = hId,
                        userId = uId,
                        nameId = card.id,
                        decision = SwipeDecision.DISMISS
                    )
                } catch (e: Exception) {
                    println("⚠️ Failed to sync dismiss: ${e.message}")
                }
            }
        }
    }
    
    fun undoSwipe() {
        val lastSwipe = _uiState.value.lastSwipe ?: return
        val hId = householdId ?: return
        val uId = userId ?: sessionManager.currentUserId.value ?: return
        
        // Restore card
        _uiState.update {
            it.copy(
                cardStack = listOf(lastSwipe.card) + it.cardStack,
                lastSwipe = null,
                isEmpty = false
            )
        }
        
        // Track analytics
        analyticsManager.trackSwipeUndone()
        
        // Remove from swiped names and local likes if it was a like
        viewModelScope.launch {
            namesRepository.undoSwipe(lastSwipe.card.name)
            
            // Remove from local likes if this was a like
            if (lastSwipe.decision == SwipeDecision.LIKE) {
                preferencesManager.removeLocalLike(lastSwipe.card.name)
            }
            
            // Sync to server
            if (!lastSwipe.card.isLocal) {
                try {
                    swipesRepository.deleteSwipe(hId, uId, lastSwipe.card.id)
                } catch (e: Exception) {
                    println("⚠️ Failed to sync undo: ${e.message}")
                }
            }
        }
    }
    
    fun dismissMatchPopup() {
        _uiState.update {
            it.copy(
                showMatchPopup = false,
                matchedName = null,
                matchedGender = null
            )
        }
    }
    
    fun reloadWithNewFilters() {
        _uiState.update { it.copy(cardStack = emptyList(), isEmpty = false) }
        loadAllNames()
    }
}
