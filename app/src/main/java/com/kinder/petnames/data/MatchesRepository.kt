package com.kinder.petnames.data

import com.kinder.petnames.domain.MatchRow
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchesRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    /**
     * Fetch all matches for a household
     */
    suspend fun fetchMatches(householdId: String): List<MatchRow> {
        return supabase.postgrest.rpc(
            function = "get_household_matches",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
            }
        ).decodeList<MatchRow>()
    }
    
    /**
     * Fetch likers for a specific name
     */
    suspend fun fetchLikers(householdId: String, nameId: String): List<String> {
        @kotlinx.serialization.Serializable
        data class LikerResult(val display_name: String?)
        
        val results = supabase.postgrest.rpc(
            function = "get_name_likers",
            parameters = buildJsonObject {
                put("p_household_id", householdId)
                put("p_name_id", nameId)
            }
        ).decodeList<LikerResult>()
        
        return results.mapNotNull { it.display_name ?: "Anonymous" }
    }
}
