package com.uth.todo

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Suppress("DEPRECATION")
@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val authManager = remember { AuthManager() }
    var message by remember { mutableStateOf("") }

    // Khởi tạo Client và Trình phóng cửa sổ Google
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
                        navController.navigate("home_screen") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    }
                }
            }
        } catch (_: Exception) {
            message = "Đã hủy đăng nhập Google"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        // --- 1. LOGO UTH TODO ---
        Box(
            modifier = Modifier
                .size(72.dp)
                .shadow(16.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFF4361EE))
                .background(Color(0xFF4361EE), RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Box(modifier = Modifier.width(22.dp).height(4.dp).background(Color.White, RoundedCornerShape(2.dp)))
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(16.dp).border(2.dp, Color.White, CircleShape))
                    Box(modifier = Modifier.width(22.dp).height(4.dp).background(Color.White.copy(alpha = 0.5f), RoundedCornerShape(2.dp)))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- 2. TIÊU ĐỀ & MÔ TẢ ---
        Text(
            text = "Chào mừng đến với\nUTH Todo",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E293B),
            lineHeight = 40.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Đăng nhập để lưu công việc và đồng bộ\ntrên mọi thiết bị của bạn.",
            fontSize = 15.sp,
            color = Color(0xFF64748B),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // --- 3. KHỐI NÚT ĐĂNG NHẬP ---
        OutlinedButton(
            onClick = {
                googleSignInClient.signOut().addOnCompleteListener {
                    googleAuthLauncher.launch(googleSignInClient.signInIntent)
                }
            },
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1E293B))
        ) {
            // Icon G giả lập
            Box(
                modifier = Modifier.size(24.dp).border(1.dp, Color(0xFFE2E8F0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("G", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF64748B))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Tiếp tục với Google", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Thanh chia cắt "hoặc"
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF1F5F9))
            Text(text = "hoặc", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 13.sp, color = Color(0xFF94A3B8))
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFF1F5F9))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nút Dùng thử
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            TextButton(
                onClick = {
                    navController.navigate("home_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            ) {
                Text("Dùng thử không cần tài khoản", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
            }
        }

        // Báo lỗi nếu có
        if (message.isNotEmpty() && message != "Đăng nhập Google thành công") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message, color = MaterialTheme.colorScheme.error, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- 4. CHÂN TRANG (FOOTER) ---
        Text(
            text = "Khi tiếp tục, bạn đồng ý với Điều khoản sử dụng và Chính\nsách quyền riêng tư của UTH Todo.",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}