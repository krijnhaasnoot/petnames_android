package com.kinder.petnames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kinder.petnames.core.PreferencesManager
import com.kinder.petnames.ui.screens.*
import com.kinder.petnames.ui.theme.PetnamesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Check if onboarded
        val isOnboarded = runBlocking { 
            preferencesManager.householdId.first() != null 
        }
        
        setContent {
            PetnamesTheme {
                PetnamesApp(
                    startDestination = if (isOnboarded) "home" else "onboarding"
                )
            }
        }
    }
}

@Composable
fun PetnamesApp(startDestination: String) {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("onboarding") {
            OnboardingScreen(
                onComplete = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        
        composable("home") {
            HomeScreen(
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToLikes = { navController.navigate("likes") },
                onNavigateToMatches = { navController.navigate("matches") },
                onNavigateToFilters = { navController.navigate("filters") }
            )
        }
        
        composable("profile") {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onReset = {
                    navController.navigate("onboarding") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable("likes") {
            LikesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable("matches") {
            MatchesScreen(
                onNavigateBack = { navController.popBackStack() },
                onMatchClick = { /* TODO: Match detail */ }
            )
        }
        
        composable("filters") {
            FiltersScreen(
                onNavigateBack = { navController.popBackStack() },
                onApply = {
                    navController.popBackStack()
                    // Trigger reload in HomeViewModel
                }
            )
        }
    }
}
