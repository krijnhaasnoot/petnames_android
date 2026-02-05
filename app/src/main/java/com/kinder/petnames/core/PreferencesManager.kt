package com.kinder.petnames.core

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.kinder.petnames.domain.Filters
import com.kinder.petnames.domain.PetLanguage
import com.kinder.petnames.domain.PetStyle
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "petnames_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    
    companion object {
        private val HOUSEHOLD_ID = stringPreferencesKey("household_id")
        private val INVITE_CODE = stringPreferencesKey("invite_code")
        private val USER_ID = stringPreferencesKey("user_id")
        private val FILTERS_GENDER = stringPreferencesKey("filters_gender")
        private val FILTERS_STARTS_WITH = stringPreferencesKey("filters_starts_with")
        private val FILTERS_MAX_LENGTH = intPreferencesKey("filters_max_length")
        private val SELECTED_LANGUAGES = stringSetPreferencesKey("selected_languages")
        private val VIEWING_LANGUAGE = stringPreferencesKey("viewing_language")
        private val ENABLED_STYLES = stringSetPreferencesKey("enabled_styles")
        private val SWIPED_NAMES = stringPreferencesKey("swiped_names")
        private val LOCAL_LIKES = stringPreferencesKey("local_likes")
    }
    
    // Household ID
    val householdId: Flow<String?> = dataStore.data.map { it[HOUSEHOLD_ID] }
    
    suspend fun setHouseholdId(id: String?) {
        dataStore.edit { prefs ->
            if (id != null) {
                prefs[HOUSEHOLD_ID] = id
            } else {
                prefs.remove(HOUSEHOLD_ID)
            }
        }
    }
    
    // Invite Code
    val inviteCode: Flow<String?> = dataStore.data.map { it[INVITE_CODE] }
    
    suspend fun setInviteCode(code: String?) {
        dataStore.edit { prefs ->
            if (code != null) {
                prefs[INVITE_CODE] = code
            } else {
                prefs.remove(INVITE_CODE)
            }
        }
    }
    
    // User ID
    val userId: Flow<String?> = dataStore.data.map { it[USER_ID] }
    
    suspend fun setUserId(id: String?) {
        dataStore.edit { prefs ->
            if (id != null) {
                prefs[USER_ID] = id
            } else {
                prefs.remove(USER_ID)
            }
        }
    }
    
    // Filters
    val filters: Flow<Filters> = dataStore.data.map { prefs ->
        Filters(
            gender = prefs[FILTERS_GENDER] ?: "any",
            startsWith = prefs[FILTERS_STARTS_WITH] ?: "any",
            maxLength = prefs[FILTERS_MAX_LENGTH] ?: 0
        )
    }
    
    suspend fun setFilters(filters: Filters) {
        dataStore.edit { prefs ->
            prefs[FILTERS_GENDER] = filters.gender
            prefs[FILTERS_STARTS_WITH] = filters.startsWith
            prefs[FILTERS_MAX_LENGTH] = filters.maxLength
        }
    }
    
    // Selected Languages
    val selectedLanguages: Flow<Set<PetLanguage>> = dataStore.data.map { prefs ->
        val codes = prefs[SELECTED_LANGUAGES] ?: setOf("nl", "en")
        codes.mapNotNull { PetLanguage.fromCode(it) }.toSet().ifEmpty { setOf(PetLanguage.EN) }
    }
    
    suspend fun setSelectedLanguages(languages: Set<PetLanguage>) {
        dataStore.edit { prefs ->
            prefs[SELECTED_LANGUAGES] = languages.map { it.code }.toSet()
        }
    }
    
    // Viewing Language
    val viewingLanguage: Flow<PetLanguage> = dataStore.data.map { prefs ->
        val code = prefs[VIEWING_LANGUAGE] ?: "en"
        PetLanguage.fromCode(code) ?: PetLanguage.EN
    }
    
    suspend fun setViewingLanguage(language: PetLanguage) {
        dataStore.edit { prefs ->
            prefs[VIEWING_LANGUAGE] = language.code
        }
    }
    
    // Enabled Styles
    val enabledStyles: Flow<Set<PetStyle>> = dataStore.data.map { prefs ->
        val codes = prefs[ENABLED_STYLES]
        if (codes == null) {
            PetStyle.entries.toSet()
        } else {
            codes.mapNotNull { PetStyle.fromCode(it) }.toSet()
        }
    }
    
    suspend fun setEnabledStyles(styles: Set<PetStyle>) {
        dataStore.edit { prefs ->
            prefs[ENABLED_STYLES] = styles.map { it.code }.toSet()
        }
    }
    
    // Swiped Names (for deduplication)
    val swipedNames: Flow<Set<String>> = dataStore.data.map { prefs ->
        val json = prefs[SWIPED_NAMES] ?: return@map emptySet()
        try {
            Json.decodeFromString<Set<String>>(json)
        } catch (e: Exception) {
            emptySet()
        }
    }
    
    suspend fun addSwipedName(name: String) {
        dataStore.edit { prefs ->
            val current = try {
                val json = prefs[SWIPED_NAMES] ?: "[]"
                Json.decodeFromString<Set<String>>(json).toMutableSet()
            } catch (e: Exception) {
                mutableSetOf()
            }
            current.add(name.lowercase())
            prefs[SWIPED_NAMES] = Json.encodeToString(current)
        }
    }
    
    suspend fun removeSwipedName(name: String) {
        dataStore.edit { prefs ->
            val current = try {
                val json = prefs[SWIPED_NAMES] ?: "[]"
                Json.decodeFromString<Set<String>>(json).toMutableSet()
            } catch (e: Exception) {
                mutableSetOf()
            }
            current.remove(name.lowercase())
            prefs[SWIPED_NAMES] = Json.encodeToString(current)
        }
    }
    
    suspend fun clearSwipedNames() {
        dataStore.edit { prefs ->
            prefs.remove(SWIPED_NAMES)
        }
    }
    
    // Clear household (for reset)
    suspend fun clearHousehold() {
        dataStore.edit { prefs ->
            prefs.remove(HOUSEHOLD_ID)
            prefs.remove(INVITE_CODE)
        }
    }
    
    // Clear all
    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
