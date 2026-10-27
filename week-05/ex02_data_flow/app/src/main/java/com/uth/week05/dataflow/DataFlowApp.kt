package com.uth.week05.dataflow

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun DataFlowApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "forgot_screen?email={email}&otp={otp}&pass={pass}") {

        composable(
            route = "forgot_screen?email={email}&otp={otp}&pass={pass}",
            arguments = listOf(
                navArgument("email") { defaultValue = "" },
                navArgument("otp") { defaultValue = "" },
                navArgument("pass") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val returnEmail = backStackEntry.arguments?.getString("email") ?: ""
            val returnOtp = backStackEntry.arguments?.getString("otp") ?: ""
            val returnPass = backStackEntry.arguments?.getString("pass") ?: ""
            ForgotPasswordScreen(navController, returnEmail, returnOtp, returnPass)
        }

        composable("verify_screen/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            VerificationScreen(navController, email)
        }

        composable("reset_screen/{email}/{otp}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            ResetPasswordScreen(navController, email, otp)
        }

        composable("confirm_screen/{email}/{otp}/{pass}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            val pass = backStackEntry.arguments?.getString("pass") ?: ""
            ConfirmScreen(navController, email, otp, pass)
        }
    }
}