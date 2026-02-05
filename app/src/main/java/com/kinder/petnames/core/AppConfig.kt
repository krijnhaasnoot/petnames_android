package com.kinder.petnames.core

import com.kinder.petnames.BuildConfig

/**
 * Configuration for Supabase and app constants
 */
object AppConfig {
    // Supabase Configuration
    val SUPABASE_URL: String = BuildConfig.SUPABASE_URL
    val SUPABASE_ANON_KEY: String = BuildConfig.SUPABASE_ANON_KEY
    
    // App Constants
    const val SWIPE_THRESHOLD = 100f
    const val CARD_ROTATION_MULTIPLIER = 0.025f
    const val ANIMATION_DURATION_MS = 200L
}
