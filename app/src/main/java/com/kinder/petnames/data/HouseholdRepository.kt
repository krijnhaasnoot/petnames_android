package com.kinder.petnames.data

import com.kinder.petnames.domain.CreateHouseholdResponse
import com.kinder.petnames.domain.HouseholdMember
import com.kinder.petnames.domain.JoinHouseholdResponse
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HouseholdRepository @Inject constructor(
    private val supabase: SupabaseClient
) {
    
    /**
     * Create a new household
     */
    suspend fun createHousehold(memberName: String): CreateHouseholdResponse {
        return supabase.postgrest.rpc(
            function = "create_household",
            parameters = buildJsonObject {
                put("p_member_name", memberName)
            }
        ).decodeSingle<CreateHouseholdResponse>()
    }
    
    /**
     * Join an existing household with invite code
     */
    suspend fun joinHousehold(inviteCode: String, memberName: String): JoinHouseholdResponse {
        return supabase.postgrest.rpc(
            function = "join_household",
            parameters = buildJsonObject {
                put("p_invite_code", inviteCode.uppercase())
                put("p_member_name", memberName)
            }
        ).decodeSingle<JoinHouseholdResponse>()
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
