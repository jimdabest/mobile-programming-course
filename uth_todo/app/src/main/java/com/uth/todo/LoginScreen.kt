package com.uth.todo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val authManager = remember { AuthManager() }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    // Khởi tạo Client và Trình phóng (Launcher) cửa sổ Google
    val googleSignInClient = remember { authManager.getGoogleSignInClient(context) }
    val googleAuthLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { token ->
                authManager.firebaseAuthWithGoogle(token) { isSuccess, resultMessage ->
                    message = resultMessage
                    if (isSuccess) {
                        // navController.navigate("home_screen")
                    }
                }
            }
        } catch (e: Exception) {
            message = "Đã hủy đăng nhập Google"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Đăng Nhập", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Mật khẩu") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                authManager.loginUser(email, password) { isSuccess, resultMessage ->
                    message = resultMessage
                    if (isSuccess) { /* Mở khóa sau */ }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
        ) {
            Text("Đăng Nhập")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hoặc", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        // Nút Đăng nhập bằng Google
        OutlinedButton(
            onClick = {
                // Kích hoạt cửa sổ chọn tài khoản Google
                googleSignInClient.signOut().addOnCompleteListener {
                    googleAuthLauncher.launch(googleSignInClient.signInIntent)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Đăng nhập bằng Google", color = Color.Black)
        }

        if (message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error)
        }
    }
}