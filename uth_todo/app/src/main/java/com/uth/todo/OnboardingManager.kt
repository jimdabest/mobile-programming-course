package com.uth.todo

import androidx.compose.runtime.*
import androidx.navigation.NavController

@Composable
fun OnboardingManager(navController: NavController) {
    var currentPage by remember { mutableIntStateOf(0) }

    OnboardingScreen(
        page = onboardingPages[currentPage],
        onSkip = {
            navController.navigate("login_screen") {
                popUpTo("onboarding_manager") { inclusive = true }
            }
        },
        onNext = {
            if (currentPage < onboardingPages.size - 1) {
                currentPage++
            } else {
                navController.navigate("login_screen") {
                    popUpTo("onboarding_manager") { inclusive = true }
                }
            }
        }
    )
}