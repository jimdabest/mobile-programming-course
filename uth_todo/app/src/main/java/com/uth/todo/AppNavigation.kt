package com.uth.todo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "start_screen") {
        composable("start_screen") {
            StartScreen(
                onTimeout = {
                    navController.navigate("onboarding_manager") {
                        popUpTo("start_screen") { inclusive = true }
                    }
                }
            )
        }
        composable("onboarding_manager") {
            OnboardingManager(navController = navController)
        }
        composable("login_screen") {
            LoginScreen(navController = navController)
        }
    }
}