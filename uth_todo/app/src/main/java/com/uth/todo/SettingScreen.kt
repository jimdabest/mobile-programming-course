package com.uth.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingScreen(navController: NavController) {
    // 1. Kiểm tra môi trường để tránh lỗi sập Preview
    val isInspection = LocalInspectionMode.current
    val user = if (isInspection) null else {
        try { FirebaseAuth.getInstance().currentUser } catch (e: Exception) { null }
    }

    val fullName = user?.displayName ?: "Acc Clone"
    val userEmail = user?.email ?: "clone@example.com"
    val avatarLetters = fullName.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC)) // Màu nền xám nhạt
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp)) // Khảng cách từ status bar

        // --- HEADER CUSTOME: Nút Back "Cài đặt" và Tiêu đề "Hồ sơ" ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4361EE), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Cài đặt", color = Color(0xFF4361EE), fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Text("Hồ sơ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- AVATAR & THÔNG TIN ---
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(Color(0xFFEEF2FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = avatarLetters, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF4361EE))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = userEmail, fontSize = 14.sp, color = Color(0xFF64748B))

        Spacer(modifier = Modifier.height(12.dp))

        // Badge: Đã liên kết tài khoản
        Row(
            modifier = Modifier
                .background(Color(0xFFECFDF5), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Đã liên kết tài khoản Google", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF10B981))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3 THẺ THỐNG KÊ ---
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(modifier = Modifier.weight(1f), number = "2", label = "Việc đã xong")
            StatCard(modifier = Modifier.weight(1f), number = "2", label = "Đang chờ")
            StatCard(modifier = Modifier.weight(1f), number = "50%", label = "Hoàn thành")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- BIỂU ĐỒ TUẦN NÀY ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tuần này", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Việc xong mỗi ngày", fontSize = 12.sp, color = Color(0xFF64748B))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Khối vẽ biểu đồ (Cứng độ cao để thanh bar mọc từ dưới lên)
                Row(
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Dữ liệu mô phỏng theo ảnh: T2 cao nhất, T3 thấp hơn, còn lại trống
                    ChartBar(day = "T2", heightFraction = 1f, color = Color(0xFFA5B4FC), isToday = false)
                    ChartBar(day = "T3", heightFraction = 0.6f, color = Color(0xFF4361EE), isToday = true)
                    ChartBar(day = "T4", heightFraction = 0.05f, color = Color(0xFFF1F5F9), isToday = false)
                    ChartBar(day = "T5", heightFraction = 0.05f, color = Color(0xFFF1F5F9), isToday = false)
                    ChartBar(day = "T6", heightFraction = 0.05f, color = Color(0xFFF1F5F9), isToday = false)
                    ChartBar(day = "T7", heightFraction = 0.05f, color = Color(0xFFF1F5F9), isToday = false)
                    ChartBar(day = "CN", heightFraction = 0.05f, color = Color(0xFFF1F5F9), isToday = false)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        // --- NÚT ĐĂNG XUẤT ---
        Button(
            onClick = {
                if (!isInspection) {
                    try { FirebaseAuth.getInstance().signOut() } catch (_: Exception) {}
                }
                navController.navigate("login_screen") {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().height(80.dp).padding(bottom = 24.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Đăng xuất", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Đăng xuất", color = Color(0xFFEF4444), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// Widget thẻ thống kê nhỏ
@Composable
fun StatCard(modifier: Modifier, number: String, label: String) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Column {
            Text(text = number, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = label, fontSize = 11.sp, color = Color(0xFF64748B))
        }
    }
}

// Widget cột biểu đồ
@Composable
fun ChartBar(day: String, heightFraction: Float, color: Color, isToday: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight() // Ép tất cả các khối bao ngoài cao bằng nhau (90.dp)
    ) {
        // Khung chứa biểu đồ
        Box(
            modifier = Modifier
                .width(22.dp)
                .weight(1f), // Tự động chiếm toàn bộ khoảng trống phía trên chữ
            contentAlignment = Alignment.BottomCenter // Canh đáy để các thanh mọc từ dưới lên
        ) {
            // Thanh màu sắc
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(heightFraction)
                    .background(color, RoundedCornerShape(4.dp))
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = day,
            fontSize = 11.sp,
            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (isToday) Color(0xFF1E293B) else Color(0xFF94A3B8)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingScreenPreview() {
    SettingScreen(navController = rememberNavController())
}