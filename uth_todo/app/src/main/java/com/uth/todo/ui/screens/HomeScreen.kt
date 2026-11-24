package com.uth.todo.ui.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.uth.todo.model.TaskResponse
import com.uth.todo.viewmodel.TodoViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: TodoViewModel = viewModel()) {
    // 1. Lắng nghe dữ liệu mạng từ ViewModel
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPreview = LocalInspectionMode.current

    // ==========================================
    // TÍNH TOÁN DỮ LIỆU ĐỂ HIỂN THỊ LÊN UI
    // ==========================================
    val displayTasks = if (isPreview) {
        listOf(
            TaskResponse(1, "Chơi Valorant 3 tiếng (Preview)", "", "life", "", "06:30", "", false, true, emptyList()),
            TaskResponse(2, "Làm bài tập - Lập trình mobile", "", "study", "", "14:00", "", false, false, emptyList())
        )
    } else tasks

    val totalTasks = displayTasks.size
    val completedTasks = displayTasks.count { it.done }
    val progressValue = if (totalTasks > 0) completedTasks.toFloat() / totalTasks else 0f
    val progressPercent = (progressValue * 100).toInt()

    val motivationText = when {
        totalTasks == 0 -> "Chưa có việc nào!"
        progressValue == 1f -> "Tuyệt vời, xong hết rồi!"
        progressValue >= 0.5f -> "Đã hơn nửa chặng, cố lên!"
        else -> "Bắt đầu làm việc thôi nào!"
    }

    val studyLeft = displayTasks.count { it.category == "study" && !it.done }
    val lifeLeft = displayTasks.count { it.category == "life" && !it.done }

    // Thông tin User
    val user = if (isPreview) null else try {
        FirebaseAuth.getInstance().currentUser
    } catch (_: Exception) {
        null
    }
    val fullName = user?.displayName ?: "Acc Clone"
    val photoUrl = user?.photoUrl
    val avatarLetters = fullName.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")
    val currentDate = SimpleDateFormat("EEEE, dd 'tháng' MM", Locale.forLanguageTag("vi-VN")).format(Date())

    // ==========================================
    // VẼ GIAO DIỆN
    // ==========================================
    Scaffold(
        containerColor = Color(0xFFF8FAFC),bottomBar = { HomeBottomBar(navController, currentRoute = "home_screen") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Mở form thêm việc */ },
                shape = CircleShape,
                containerColor = Color(0xFF3B82F6),
                contentColor = Color.White,
                modifier = Modifier
                    .size(56.dp)
                    .offset(y = 60.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm")
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. Header (Avatar, Ngày, Lời chào, Chuông)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (photoUrl != null) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile_screen") }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFDBEAFE), CircleShape)
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile_screen") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = avatarLetters, fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A), fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = currentDate, fontSize = 12.sp, color = Color(0xFF64748B))
                    Text(text = "Chào $fullName!", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
                }
                Box(contentAlignment = Alignment.TopEnd) {
                    Icon(Icons.Default.Notifications, contentDescription = "Thông báo", modifier = Modifier.size(28.dp), tint = Color(0xFF64748B))
                    Box(modifier = Modifier.size(10.dp).background(Color.Red, CircleShape).border(2.dp, Color(0xFFF8FAFC), CircleShape))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Thẻ Tiến độ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4361EE), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Tiến độ hôm nay", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "$completedTasks/$totalTasks việc", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = motivationText, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
                        CircularProgressIndicator(progress = { progressValue }, modifier = Modifier.fillMaxSize(), color = Color.White, trackColor = Color.White.copy(alpha = 0.2f), strokeWidth = 6.dp)
                        Text(text = "${progressPercent}%", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Danh mục
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CategoryCard(modifier = Modifier.weight(1f), title = "Học tập", count = "$studyLeft việc còn lại", iconBg = Color(0xFFEEF2FF), iconTint = Color(0xFF4361EE))
                CategoryCard(modifier = Modifier.weight(1f), title = "Cuộc sống", count = "$lifeLeft việc còn lại", iconBg = Color(0xFFFFF7ED), iconTint = Color(0xFFF59E0B))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Tiêu đề Danh sách
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Việc hôm nay", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text(text = "Xem tất cả", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4361EE))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Danh sách Task
            if (isLoading && !isPreview) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4361EE))
                }
            } else {
                displayTasks.forEach { task ->
                    TaskItem(
                        taskId = task.id,
                        navController = navController,
                        title = task.title,
                        category = if (task.category == "study") "Học tập" else "Cuộc sống",
                        time = task.time,
                        isDone = task.done
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CategoryCard(modifier: Modifier, title: String, count: String, iconBg: Color, iconTint: Color) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Box(modifier = Modifier.size(36.dp).background(iconBg, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Outlined.Circle, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = count, fontSize = 12.sp, color = Color(0xFF94A3B8))
        }
    }
}

@Composable
fun TaskItem(
    title: String,
    category: String,
    time: String,
    isDone: Boolean,
    taskId: Int,
    navController: NavController
) {
    val catColor = if (category == "Học tập") Color(0xFF4361EE) else Color(0xFFF59E0B)
    val catBg = if (category == "Học tập") Color(0xFFEEF2FF) else Color(0xFFFFF7ED)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { navController.navigate("task_detail_screen/$taskId") }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Outlined.Circle,
            contentDescription = null,
            tint = if (isDone) Color(0xFF4361EE) else Color(0xFFCBD5E1),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isDone) FontWeight.Medium else FontWeight.SemiBold,
                color = if (isDone) Color(0xFF94A3B8) else Color(0xFF1E293B),
                textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.background(catBg, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                    Text(text = category, color = catColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = time, color = Color(0xFF94A3B8), fontSize = 11.sp)
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.size(20.dp))
    }
}

@Composable
fun HomeBottomBar(navController: NavController, currentRoute: String = "home_screen") {
    BottomAppBar(
        containerColor = Color.White,
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.shadow(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Nút Trang chủ
            BottomIcon(
                icon = Icons.Default.Home,
                label = "Trang chủ",
                isSelected = currentRoute == "home_screen", // Sáng lên nếu đang ở Home
                onClick = {
                    if (currentRoute != "home_screen") {
                        // Chuyển về Home và xóa các trang phụ xếp chồng lên nhau
                        navController.navigate("home_screen") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    }
                }
            )

            // 2. Nút Danh sách
            BottomIcon(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Danh sách",
                isSelected = currentRoute == "task_list_screen", // Sáng lên nếu đang ở List
                onClick = {
                    if (currentRoute != "task_list_screen") {
                        navController.navigate("task_list_screen")
                    }
                }
            )

            Spacer(modifier = Modifier.width(48.dp)) // Khoảng trống cho nút FAB (Thêm việc) chính giữa

            // 3. Nút Thông báo (Tạm thời chưa có màn hình)
            BottomIcon(icon = Icons.Default.Notifications, label = "Thông báo", isSelected = false)

            // 4. Nút Cài đặt (Profile)
            BottomIcon(
                icon = Icons.Default.Settings,
                label = "Cài đặt",
                isSelected = currentRoute == "profile_screen",
                onClick = {
                    navController.navigate("profile_screen")
                }
            )
        }
    }
}

@Composable
fun BottomIcon(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Icon(imageVector = icon, contentDescription = label, tint = if (isSelected) Color(0xFF4361EE) else Color(0xFF94A3B8), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, color = if (isSelected) Color(0xFF4361EE) else Color(0xFF94A3B8), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}