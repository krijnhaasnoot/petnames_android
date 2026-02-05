package com.kinder.petnames.data

import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.domain.NameCard
import com.kinder.petnames.domain.NameSet
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NamesRepository @Inject constructor(
    private val supabase: SupabaseClient,
    private val localNamesProvider: LocalNamesProvider,
    private val preferencesManager: PreferencesManager
) {
    
    /**
     * Fetch all name sets
     */
    suspend fun fetchNameSets(): List<NameSet> {
        return try {
            supabase.postgrest
                .from("name_sets")
                .select()
                .decodeList<NameSet>()
        } catch (e: Exception) {
            // Fallback to local sets
            localNamesProvider.getNameSets()
        }
    }
    
    /**
     * Get next batch of names (offline-first)
     */
    suspend fun getNextNames(
        householdId: String,
        enabledSetIds: List<String>,
        gender: String,
        startsWith: String,
        maxLength: Int,
        limit: Int = 10000,
        excludeNames: List<String> = emptyList()
    ): List<NameCard> {
        // Get swiped names from preferences
        val swipedNames = preferencesManager.swipedNames.first()
        val allExcluded = swipedNames + excludeNames.map { it.lowercase() }
        
        // Get local names
        val localNames = localNamesProvider.getNames(
            enabledSetIds = enabledSetIds,
            gender = gender,
            startsWith = startsWith,
            maxLength = maxLength,
            excludeNames = allExcluded,
            limit = limit
        )
        
        return localNames.map { entry ->
            NameCard(
                id = UUID.randomUUID().toString(),
                name = entry.name,
                gender = entry.gender,
                setTitle = entry.setTitle,
                setId = entry.setSlug,
                isLocal = true
            )
        }
    }
    
    /**
     * Mark a name as swiped (for deduplication)
     */
    suspend fun markNameAsSwiped(name: String) {
        preferencesManager.addSwipedName(name)
    }
    
    /**
     * Undo swipe tracking
     */
    suspend fun undoSwipe(name: String) {
        preferencesManager.removeSwipedName(name)
    }
    
    /**
     * Clear all swiped names
     */
    suspend fun clearSwipedNames() {
        preferencesManager.clearSwipedNames()
    }
}
