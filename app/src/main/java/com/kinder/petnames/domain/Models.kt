package com.kinder.petnames.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

// MARK: - Name Card

@Serializable
data class NameCard(
    @SerialName("name_id") val id: String,
    val name: String,
    val gender: String,
    @SerialName("set_title") val setTitle: String,
    @SerialName("set_id") val setId: String,
    @SerialName("is_local") val isLocal: Boolean = false
)

// MARK: - Name Set

@Serializable
data class NameSet(
    val id: String,
    val slug: String,
    val title: String,
    @SerialName("is_free") val isFree: Boolean = true,
    val language: String? = null,
    val style: String? = null,
    val description: String? = null
)

// MARK: - Swipe Decision

enum class SwipeDecision(val value: String) {
    LIKE("like"),
    DISMISS("dismiss")
}

// MARK: - Swipe Record

@Serializable
data class SwipeRecord(
    @SerialName("household_id") val householdId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("name_id") val nameId: String,
    val decision: String
)

// MARK: - Liked Name Row

@Serializable
data class LikedNameRow(
    @SerialName("name_id") val nameId: String,
    val name: String,
    val gender: String,
    @SerialName("set_title") val setTitle: String
) {
    val id: String get() = nameId
}

// MARK: - Match Row

@Serializable
data class MatchRow(
    @SerialName("name_id") val nameId: String,
    val name: String,
    val gender: String,
    @SerialName("likes_count") val likesCount: Int
) {
    val id: String get() = nameId
}

// MARK: - Household Responses

@Serializable
data class CreateHouseholdResponse(
    @SerialName("household_id") val householdId: String,
    @SerialName("invite_code") val inviteCode: String
)

@Serializable
data class JoinHouseholdResponse(
    @SerialName("household_id") val householdId: String
)

// MARK: - Household

@Serializable
data class Household(
    val id: String,
    @SerialName("invite_code") val inviteCode: String
)

// MARK: - Profile

@Serializable
data class Profile(
    val id: String,
    @SerialName("display_name") val displayName: String? = null
)

// MARK: - Household Member

@Serializable
data class HouseholdMember(
    val id: String,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("created_at") val createdAt: String? = null
) {
    val displayLabel: String
        get() = if (!displayName.isNullOrBlank()) {
            displayName
        } else {
            "User ${id.take(8)}"
        }
}

// MARK: - Filters

data class Filters(
    val gender: String = "any",
    val startsWith: String = "any",
    val maxLength: Int = 0
) {
    companion object {
        val DEFAULT = Filters()
        val GENDER_OPTIONS = listOf("any", "male", "female", "neutral")
        val STARTS_WITH_OPTIONS = listOf("any") + ('a'..'z').map { it.toString() }
    }
}

// MARK: - Swipe Counts

data class SwipeCounts(
    val likes: Int,
    val dismisses: Int
)

// MARK: - Pet Language

enum class PetLanguage(val code: String, val displayName: String, val flag: String, val slugPrefix: String) {
    NL("nl", "Nederlands", "🇳🇱", "dutch"),
    EN("en", "English", "🇬🇧", "english"),
    DE("de", "Deutsch", "🇩🇪", "german"),
    FR("fr", "Français", "🇫🇷", "french"),
    ES("es", "Español", "🇪🇸", "spanish"),
    IT("it", "Italiano", "🇮🇹", "italian"),
    SV("sv", "Svenska", "🇸🇪", "swedish"),
    NO("no", "Norsk", "🇳🇴", "norwegian"),
    DA("da", "Dansk", "🇩🇰", "danish"),
    FI("fi", "Suomi", "🇫🇮", "finnish");
    
    val shortName: String get() = code.uppercase()
    
    companion object {
        fun fromCode(code: String): PetLanguage? = entries.find { it.code == code }
    }
}

// MARK: - Pet Style

enum class PetStyle(val code: String, val icon: String, val colorHex: String) {
    CUTE("cute", "❤️", "ec4899"),
    STRONG("strong", "⚡", "f97316"),
    CLASSIC("classic", "📚", "92400e"),
    FUNNY("funny", "😄", "eab308"),
    VINTAGE("vintage", "🕰️", "a855f7"),
    NATURE("nature", "🍃", "22c55e"),
    PETNICKNAMES("petnicknames", "⭐", "3b82f6");
    
    fun title(language: PetLanguage): String = when (language) {
        PetLanguage.NL -> when (this) {
            CUTE -> "Lief & Schattig"
            STRONG -> "Kort & Stoer"
            CLASSIC -> "Klassiek"
            FUNNY -> "Speels & Grappig"
            VINTAGE -> "Nostalgisch"
            NATURE -> "Natuur"
            PETNICKNAMES -> "Koosnamen"
        }
        else -> when (this) {
            CUTE -> "Cute & Sweet"
            STRONG -> "Short & Strong"
            CLASSIC -> "Classic"
            FUNNY -> "Funny & Playful"
            VINTAGE -> "Vintage"
            NATURE -> "Nature Inspired"
            PETNICKNAMES -> "Pet Nicknames"
        }
    }
    
    companion object {
        fun fromCode(code: String): PetStyle? = entries.find { it.code == code }
    }
}

// MARK: - Bundled Names Data (for offline JSON)

@Serializable
data class BundledNamesData(
    val version: Int,
    val lastUpdated: String,
    val nameSets: List<BundledNameSet>
)

@Serializable
data class BundledNameSet(
    val slug: String,
    val title: String,
    val description: String,
    val language: String,
    val style: String,
    val names: List<BundledName>
)

@Serializable
data class BundledName(
    val name: String,
    val gender: String
)

// MARK: - Local Name Entry

data class LocalNameEntry(
    val name: String,
    val gender: String,
    val setSlug: String,
    val setTitle: String,
    val language: String,
    val style: String
)
