package com.kinder.petnames.core

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsManager @Inject constructor() {
    
    private val analytics: FirebaseAnalytics = Firebase.analytics
    
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
        analytics.logEvent("onboarding_completed") {
            param("method", method)
        }
    }
    
    // MARK: - Swiping
    
    fun trackNameSwiped(decision: String, name: String, gender: String) {
        analytics.logEvent("name_swiped") {
            param("decision", decision)
            param("gender", gender)
        }
        
        if (decision == "like") {
            trackNameLiked(name, gender)
        } else {
            trackNameDismissed(name, gender)
        }
    }
    
    private fun trackNameLiked(name: String, gender: String) {
        analytics.logEvent("name_liked") {
            param(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param("gender", gender)
        }
    }
    
    private fun trackNameDismissed(name: String, gender: String) {
        analytics.logEvent("name_dismissed") {
            param(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param("gender", gender)
        }
    }
    
    fun trackSwipeUndone() {
        analytics.logEvent("swipe_undone", null)
    }
    
    fun trackCardStackEmpty(filtersActive: Boolean) {
        analytics.logEvent("card_stack_empty") {
            param("filters_active", filtersActive.toString())
        }
    }
    
    // MARK: - Filters
    
    fun trackFilterChanged(filterType: String, value: String) {
        analytics.logEvent("filter_changed") {
            param("filter_type", filterType)
            param("value", value)
        }
    }
    
    fun trackLanguagesSelected(languages: List<String>) {
        analytics.logEvent("languages_selected") {
            param("count", languages.size.toLong())
            param("languages", languages.joinToString(","))
        }
        setUserProperty(languages.size.toString(), "languages_count")
    }
    
    fun trackStylesEnabled(styles: List<String>) {
        analytics.logEvent("styles_enabled") {
            param("count", styles.size.toLong())
            param("styles", styles.joinToString(","))
        }
    }
    
    // MARK: - Matches
    
    fun trackMatchCreated(name: String, gender: String) {
        analytics.logEvent("match_created") {
            param(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
            param("gender", gender)
        }
    }
    
    fun trackMatchViewed(name: String) {
        analytics.logEvent("match_viewed") {
            param(FirebaseAnalytics.Param.ITEM_ID, name.lowercase())
            param(FirebaseAnalytics.Param.ITEM_NAME, name)
        }
    }
    
    fun trackMatchShared(shareType: String, name: String? = null) {
        analytics.logEvent("match_shared") {
            param("share_type", shareType)
            name?.let { param(FirebaseAnalytics.Param.ITEM_NAME, it) }
        }
    }
    
    // MARK: - Features
    
    fun trackLikesViewed(count: Int) {
        analytics.logEvent("likes_viewed") {
            param("count", count.toLong())
        }
    }
    
    fun trackProfileViewed() {
        analytics.logEvent("profile_viewed", null)
    }
    
    fun trackMatchesListViewed(count: Int) {
        analytics.logEvent("matches_list_viewed") {
            param("count", count.toLong())
        }
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
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }
    
    // MARK: - User Properties
    
    private fun setUserProperty(value: String?, name: String) {
        analytics.setUserProperty(name, value)
    }
    
    fun setHouseholdSize(size: Int) {
        setUserProperty(size.toString(), "household_size")
    }
}
