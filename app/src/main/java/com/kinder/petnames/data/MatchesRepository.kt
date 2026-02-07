package com.kinder.petnames.data

import com.kinder.petnames.domain.MatchRow
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
private data class MatchViewRow(
    @SerialName("name_id") val nameId: String,
    @SerialName("likes_count") val likesCount: Int,
    val names: MatchNameData
)

@Serializable
private data class MatchNameData(
    val name: String,
    val gender: String
)

@Serializable
private data class SwipeRecord(
    @SerialName("user_id") val userId: String
)

@Serializable
private data class ProfileRecord(
    val id: String,
    @SerialName("display_name") val displayName: String?
)

@Singleton
class MatchesRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    /**
     * Fetch all matches for a household (names liked by 2+ members)
     * Uses direct query on household_matches view like iOS
     */
    suspend fun fetchMatches(householdId: String): List<MatchRow> {
        println("📋 Fetching matches for household: $householdId")
        
        return try {
            val matches: List<MatchViewRow> = supabase.postgrest
                .from("household_matches")
                .select(Columns.raw("name_id, likes_count, names!inner(name, gender)")) {
                    filter { eq("household_id", householdId) }
                    order("likes_count", Order.DESCENDING)
                }
                .decodeList()
            
            println("✅ Got ${matches.size} matches from server")
            
            matches.map { match ->
                MatchRow(
                    nameId = match.nameId,
                    name = match.names.name,
                    gender = match.names.gender,
                    likesCount = match.likesCount
                )
            }
        } catch (e: Exception) {
            println("❌ Error fetching matches: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Fetch likers for a specific name (who liked this name in the household)
     */
    suspend fun fetchLikers(householdId: String, nameId: String): List<String> {
        return try {
            // First get the user_ids who liked this name
            val swipes: List<SwipeRecord> = supabase.postgrest
                .from("swipes")
                .select(Columns.raw("user_id")) {
                    filter {
                        eq("household_id", householdId)
                        eq("name_id", nameId)
                        eq("decision", "like")
                    }
                }
                .decodeList()
            
            if (swipes.isEmpty()) return emptyList()
            
            // Then fetch profiles for those users
            val userIds = swipes.map { it.userId }
            
            val profiles: List<ProfileRecord> = supabase.postgrest
                .from("profiles")
                .select(Columns.raw("id, display_name")) {
                    filter { isIn("id", userIds) }
                }
                .decodeList()
            
            profiles.map { profile ->
                profile.displayName ?: "Anonymous"
            }
        } catch (e: Exception) {
            println("❌ Error fetching likers: ${e.message}")
            emptyList()
        }
    }
}
