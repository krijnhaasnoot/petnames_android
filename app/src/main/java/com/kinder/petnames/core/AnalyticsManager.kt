package com.kinder.petnames.core

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val analytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }
    
    // MARK: - Onboarding & Households
    
    fun trackHouseholdCreated() {
        analytics.logEvent("household_created", null)
        setUserProperty("creator", "household_role")
    }
    
    fun trackHouseholdJoined() {
        analytics.logEvent("household_joined", null)
        setUserProperty("joiner", "household_role")
    }
    
    fun trackOnboardingCompleted(method: String) {
        analytics.logEvent("onboarding_completed", Bundle().apply {
            putString("method", method)
        })
    }
    
    // MARK: - Swiping
    
    fun trackNameSwiped(decision: String, name: String, gender: String) {
        analytics.logEvent("name_swiped", Bundle().apply {
            putString("decision", decision)
            putString("gender", gender)
        })
        
        if (decision == "like") {
            trackNameLiked(name, gender)
        } else {
            trackNameDismissed(name, gender)
        }
    }
    
    private fun trackNameLiked(name: String, gender: String) {
        analytics.logEvent("name_liked", Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            putString(FirebaseAnalytics.Param.ITEM_NAME, name)
            putString("gender", gender)
        })
    }
    
    private fun trackNameDismissed(name: String, gender: String) {
        analytics.logEvent("name_dismissed", Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            putString(FirebaseAnalytics.Param.ITEM_NAME, name)
            putString("gender", gender)
        })
    }
    
    fun trackSwipeUndone() {
        analytics.logEvent("swipe_undone", null)
    }
    
    fun trackCardStackEmpty(filtersActive: Boolean) {
        analytics.logEvent("card_stack_empty", Bundle().apply {
            putString("filters_active", filtersActive.toString())
        })
    }
    
    // MARK: - Filters
    
    fun trackFilterChanged(filterType: String, value: String) {
        analytics.logEvent("filter_changed", Bundle().apply {
            putString("filter_type", filterType)
            putString("value", value)
        })
    }
    
    fun trackLanguagesSelected(languages: List<String>) {
        analytics.logEvent("languages_selected", Bundle().apply {
            putLong("count", languages.size.toLong())
            putString("languages", languages.joinToString(","))
        })
        setUserProperty(languages.size.toString(), "languages_count")
    }
    
    fun trackStylesEnabled(styles: List<String>) {
        analytics.logEvent("styles_enabled", Bundle().apply {
            putLong("count", styles.size.toLong())
            putString("styles", styles.joinToString(","))
        })
    }

    // MARK: - Matches
    
    fun trackMatchCreated(name: String, gender: String) {
        analytics.logEvent("match_created", Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            putString(FirebaseAnalytics.Param.ITEM_NAME, name)
            putString("gender", gender)
        })
    }
    
    fun trackMatchViewed(name: String) {
        analytics.logEvent("match_viewed", Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        })
    }
    
    fun trackMatchShared(shareType: String, name: String? = null) {
        analytics.logEvent("match_shared", Bundle().apply {
            putString("share_type", shareType)
            name?.let { putString(FirebaseAnalytics.Param.ITEM_NAME, it) }
        })
    }
    
    // MARK: - Features
    
    fun trackLikesViewed(count: Int) {
        analytics.logEvent("likes_viewed", Bundle().apply {
            putLong("count", count.toLong())
        })
    }
    
    fun trackProfileViewed() {
        analytics.logEvent("profile_viewed", null)
    }
    
    fun trackMatchesListViewed(count: Int) {
        analytics.logEvent("matches_list_viewed", Bundle().apply {
            putLong("count", count.toLong())
        })
    }
    
    // MARK: - Notifications
    
    fun trackNotificationPermissionGranted() {
        analytics.logEvent("notification_permission_granted", null)
        setUserProperty("true", "notifications_enabled")
    }
    
    fun trackNotificationPermissionDenied() {
        analytics.logEvent("notification_permission_denied", null)
        setUserProperty("false", "notifications_enabled")
    }
    
    // MARK: - Screen Views
    
    fun trackScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        })
    }
    
    // MARK: - User Properties
    
    private fun setUserProperty(value: String?, name: String) {
        analytics.setUserProperty(name, value)
    }
    
    fun setHouseholdSize(size: Int) {
        setUserProperty(size.toString(), "household_size")
    }
}
