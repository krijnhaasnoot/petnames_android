package com.kinder.petnames.data

import com.kinder.petnames.domain.CreateHouseholdResponse
import com.kinder.petnames.domain.HouseholdMember
import com.kinder.petnames.domain.JoinHouseholdResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HouseholdRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    private val json = Json { ignoreUnknownKeys = true }
    
    /**
     * Create a new household
     * Uses display_name parameter (matching iOS and Supabase function signature)
     */
    suspend fun createHousehold(memberName: String): CreateHouseholdResponse {
        val result = supabase.postgrest.rpc(
            function = "create_household",
            parameters = buildJsonObject {
                put("display_name", memberName.ifBlank { null })
            }
        )
        
        // Get raw response body and parse manually
        val responseBody = result.data
        println("📦 Create household response: $responseBody")
        
        return json.decodeFromString<CreateHouseholdResponse>(responseBody)
    }
    
    /**
     * Join an existing household with invite code
     * Uses invite_code parameter (matching iOS and Supabase function signature)
     */
    suspend fun joinHousehold(inviteCode: String, memberName: String): JoinHouseholdResponse {
        val result = supabase.postgrest.rpc(
            function = "join_household",
            parameters = buildJsonObject {
                put("invite_code", inviteCode.uppercase())
            }
        )
        
        // Get raw response body and parse manually
        val responseBody = result.data
        println("📦 Join household response: $responseBody")
        
        return json.decodeFromString<JoinHouseholdResponse>(responseBody)
    }
    
    /**
     * Fetch invite code for a household
     */
    suspend fun fetchInviteCode(householdId: String): String? {
        val result = supabase.postgrest
            .from("households")
            .select {
                filter { eq("id", householdId) }
            }
            .decodeSingleOrNull<Map<String, String>>()
        
        return result?.get("invite_code")
    }
    
    /**
     * Fetch household members
     */
    suspend fun fetchHouseholdMembers(householdId: String): List<HouseholdMember> {
        return supabase.postgrest
            .from("profiles")
            .select {
                filter { eq("household_id", householdId) }
            }
            .decodeList<HouseholdMember>()
    }
    
    /**
     * Update display name
     */
    suspend fun updateDisplayName(userId: String, displayName: String) {
        supabase.postgrest
            .from("profiles")
            .update(mapOf("display_name" to displayName)) {
                filter { eq("id", userId) }
            }
    }
}
