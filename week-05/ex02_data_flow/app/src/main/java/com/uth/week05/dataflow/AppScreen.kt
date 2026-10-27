package com.uth.week05.dataflow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// --- MÀN HÌNH 1 ---
@Composable
fun ForgotPasswordScreen(navController: NavController, returnEmail: String, returnOtp: String, returnPass: String) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopHeader(navController, showBackButton = false)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Forget Password?", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Enter your Email, we will send you a verification code.", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Your Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (email.isNotBlank()) {
                    errorMessage = ""
                    navController.navigate("verify_screen/$email")
                } else {
                    errorMessage = "Vui lòng nhập Email!"
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) { Text("Next", fontSize = 16.sp, color = Color.White) }

        if (returnEmail.isNotEmpty()) {
            Spacer(modifier = Modifier.height(48.dp))
            Box(
                modifier = Modifier.fillMaxWidth().background(Color(0xFFDBEAFE), RoundedCornerShape(16.dp)).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("email: $returnEmail", color = Color.Red, fontWeight = FontWeight.Bold)
                    Text("otp: $returnOtp", color = Color.Red, fontWeight = FontWeight.Bold)
                    Text("pass: $returnPass", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- MÀN HÌNH 2 ---
@Composable
fun VerificationScreen(navController: NavController, email: String) {
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopHeader(navController, showBackButton = true)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Verify Code", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Enter the code we just sent you on your registered Email\n$email", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("Enter OTP") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (otp == "1111") navController.navigate("reset_screen/$email/$otp")
                else errorMessage = "OTP không hợp lệ! (Vui lòng nhập 1111)"
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) { Text("Next", fontSize = 16.sp, color = Color.White) }
    }
}

// --- MÀN HÌNH 3 ---
@Composable
fun ResetPasswordScreen(navController: NavController, email: String, otp: String) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopHeader(navController, showBackButton = true)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Create new password", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your new password must be different from previously used password", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it }, label = { Text("Confirm Password") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth(), singleLine = true)
        if (errorMessage.isNotEmpty()) Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                if (password.isNotBlank() && password == confirmPassword) navController.navigate("confirm_screen/$email/$otp/$password")
                else errorMessage = "Mật khẩu không khớp hoặc bị trống!"
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) { Text("Next", fontSize = 16.sp, color = Color.White) }
    }
}

// --- MÀN HÌNH 4 ---
@Composable
fun ConfirmScreen(navController: NavController, email: String, otp: String, pass: String) {
    Column(modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        TopHeader(navController, showBackButton = true)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Confirm", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        Text("We are here to help you!", textAlign = TextAlign.Center, color = Color.Gray, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(value = email, onValueChange = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = otp, onValueChange = {}, enabled = false, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = {}, enabled = false, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                navController.navigate("forgot_screen?email=$email&otp=$otp&pass=$pass") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) { Text("Submit", fontSize = 16.sp, color = Color.White) }
    }
}