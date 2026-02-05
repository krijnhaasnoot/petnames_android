package com.kinder.petnames.data

import com.kinder.petnames.domain.LikedNameRow
import com.kinder.petnames.domain.SwipeCounts
import com.kinder.petnames.domain.SwipeDecision
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwipesRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    /**
     * Insert a swipe decision
     */
    suspend fun insertSwipe(
        householdId: String,
        userId: String,
        nameId: String,
        decision: SwipeDecision
    ) {
        supabase.postgrest
            .from("swipes")
            .upsert(
                mapOf(
                    "household_id" to householdId,
                    "user_id" to userId,
                    "name_id" to nameId,
                    "decision" to decision.value
                )
            )
    }
    
    /**
     * Insert swipe by name (for local names that need to be looked up)
     * Returns true if this creates a match
     */
    suspend fun insertSwipeByName(
        householdId: String,
        userId: String,
        name: String,
        gender: String,
        decision: SwipeDecision
    ): Boolean {
        @Serializable
        data class SwipeByNameResult(val is_match: Boolean = false)
        
        val result = supabase.postgrest.rpc(
            function = "swipe_by_name",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
                put("p_name", name)
                put("p_gender", gender)
                put("p_decision", decision.value)
            }
        ).decodeSingleOrNull<SwipeByNameResult>()
        
        return result?.is_match ?: false
    }
    
    /**
     * Delete a swipe (for undo)
     */
    suspend fun deleteSwipe(householdId: String, userId: String, nameId: String) {
        supabase.postgrest
            .from("swipes")
            .delete {
                filter {
                    eq("household_id", householdId)
                    eq("user_id", userId)
                    eq("name_id", nameId)
                }
            }
    }
    
    /**
     * Check if a name creates a match
     */
    suspend fun checkForMatchByName(
        householdId: String,
        name: String,
        userId: String
    ): Boolean {
        @Serializable
        data class MatchCheckResult(val is_match: Boolean = false)
        
        val result = supabase.postgrest.rpc(
            function = "check_match_by_name",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
                put("p_name", name)
                put("p_user_id", userId)
            }
        ).decodeSingleOrNull<MatchCheckResult>()
        
        return result?.is_match ?: false
    }
    
    /**
     * Fetch user's liked names
     */
    suspend fun fetchLikes(householdId: String, userId: String): List<LikedNameRow> {
        return supabase.postgrest.rpc(
            function = "get_user_likes",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
                put("p_user_id", userId)
            }
        ).decodeList<LikedNameRow>()
    }
    
    /**
     * Fetch swipe counts
     */
    suspend fun fetchCounts(householdId: String, userId: String): SwipeCounts {
        @Serializable
        data class CountsResult(val likes: Int = 0, val dismisses: Int = 0)
        
        val result = supabase.postgrest.rpc(
            function = "get_swipe_counts",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
                put("p_user_id", userId)
            }
        ).decodeSingleOrNull<CountsResult>()
        
        return SwipeCounts(
            likes = result?.likes ?: 0,
            dismisses = result?.dismisses ?: 0
        )
    }
}
