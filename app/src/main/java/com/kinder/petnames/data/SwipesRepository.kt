package com.kinder.petnames.data

import com.kinder.petnames.domain.LikedNameRow
import com.kinder.petnames.domain.SwipeCounts
import com.kinder.petnames.domain.SwipeDecision
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

// Data classes for Supabase responses
@Serializable
private data class SwipeWithName(
    @SerialName("name_id") val nameId: String,
    val names: NameDataResponse? = null
)

@Serializable
private data class NameDataResponse(
    val name: String,
    val gender: String,
    @SerialName("name_sets") val nameSets: SetDataResponse? = null
)

@Serializable
private data class SetDataResponse(
    val title: String
)

@Serializable
private data class SwipeByNameResult(
    @SerialName("is_match") val isMatch: Boolean = false
)

@Serializable
private data class MatchCheckResult(
    @SerialName("is_match") val isMatch: Boolean = false
)

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
        println("📝 Inserting swipe: nameId=$nameId, decision=${decision.value}")
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
        println("✅ Swipe inserted")
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
        println("📝 Inserting swipe by name: $name, decision=${decision.value}")
        
        return try {
            val result = supabase.postgrest.rpc(
                function = "swipe_by_name",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_name", name)
                    put("p_gender", gender)
                    put("p_decision", decision.value)
                }
            ).decodeSingleOrNull<SwipeByNameResult>()
            
            println("✅ Swipe by name result: isMatch=${result?.isMatch}")
            result?.isMatch ?: false
        } catch (e: Exception) {
            println("⚠️ swipe_by_name failed: ${e.message}")
            // Fallback: just return false, the swipe should still be tracked locally
            false
        }
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
        return try {
            val result = supabase.postgrest.rpc(
                function = "check_match_by_name",
                parameters = buildJsonObject {
                    put("p_household_id", householdId)
                    put("p_name", name)
                    put("p_user_id", userId)
                }
            ).decodeSingleOrNull<MatchCheckResult>()
            
            result?.isMatch ?: false
        } catch (e: Exception) {
            println("⚠️ check_match_by_name failed: ${e.message}")
            false
        }
    }
    
    /**
     * Fetch user's liked names using direct query (like iOS)
     */
    suspend fun fetchLikes(householdId: String, userId: String): List<LikedNameRow> {
        println("📋 Fetching likes for household=$householdId, user=$userId")
        
        return try {
            val swipes: List<SwipeWithName> = supabase.postgrest
                .from("swipes")
                .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("""
                    name_id,
                    names!inner(name, gender, name_sets!inner(title))
                """.trimIndent())) {
                    filter {
                        eq("household_id", householdId)
                        eq("user_id", userId)
                        eq("decision", "like")
                    }
                }
                .decodeList<SwipeWithName>()
            
            println("✅ Got ${swipes.size} likes from server")
            
            swipes.mapNotNull { swipe ->
                swipe.names?.let { nameData ->
                    LikedNameRow(
                        nameId = swipe.nameId,
                        name = nameData.name,
                        gender = nameData.gender,
                        setTitle = nameData.nameSets?.title ?: "Unknown"
                    )
                }
            }
        } catch (e: Exception) {
            println("❌ Error fetching likes: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Fetch swipe counts
     */
    suspend fun fetchCounts(householdId: String, userId: String): SwipeCounts {
        return try {
            // Count likes
            val likesCount = supabase.postgrest
                .from("swipes")
                .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("count")) {
                    filter {
                        eq("household_id", householdId)
                        eq("user_id", userId)
                        eq("decision", "like")
                    }
                    count(io.github.jan.supabase.postgrest.query.Count.EXACT)
                }
                .countOrNull() ?: 0
            
            // Count dismisses
            val dismissesCount = supabase.postgrest
                .from("swipes")
                .select(columns = io.github.jan.supabase.postgrest.query.Columns.raw("count")) {
                    filter {
                        eq("household_id", householdId)
                        eq("user_id", userId)
                        eq("decision", "dismiss")
                    }
                    count(io.github.jan.supabase.postgrest.query.Count.EXACT)
                }
                .countOrNull() ?: 0
            
            SwipeCounts(
                likes = likesCount.toInt(),
                dismisses = dismissesCount.toInt()
            )
        } catch (e: Exception) {
            println("❌ Error fetching counts: ${e.message}")
            SwipeCounts(likes = 0, dismisses = 0)
        }
    }
}
