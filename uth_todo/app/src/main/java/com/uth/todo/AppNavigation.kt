package com.uth.todo

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uth.todo.manager.OnboardingManager
import com.uth.todo.ui.screens.HomeScreen
import com.uth.todo.ui.screens.LoginScreen
import com.uth.todo.ui.screens.SettingScreen
import com.uth.todo.ui.screens.StartScreen
import com.uth.todo.ui.screens.TaskDetailScreen
import com.uth.todo.ui.screens.TaskListScreen

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
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("profile_screen") {
            SettingScreen(navController = navController)
        }
        composable(
            route = "task_detail_screen/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: 0
            // Màn hình chi tiết sẽ được tạo ở bước sau
            TaskDetailScreen(navController = navController, taskId = taskId)
        }
        composable("task_list_screen") {
            TaskListScreen(navController = navController)
        }
    }
}