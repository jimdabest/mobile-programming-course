package com.uth.todo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.uth.todo.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(navController: NavController, taskId: Int, viewModel: TodoViewModel = viewModel()) {
    val task by viewModel.selectedTask.collectAsState()
    val isLoading by viewModel.isDetailLoading.collectAsState()

    // Tự động gọi API lấy chi tiết Task khi màn hình vừa được mở
    LaunchedEffect(taskId) {
        viewModel.fetchTaskDetail(taskId)
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { navController.popBackStack() } // Trở về trang trước
                            .padding(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF4361EE))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trang chủ", color = Color(0xFF4361EE), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                },
                actions = {
                    TextButton(onClick = { /* TODO: Chuyển sang màn hình Sửa */ }) {
                        Text("Sửa", color = Color(0xFF4361EE), fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF4361EE))
            }
        } else if (task != null) {
            val t = task!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                // 1. Tiêu đề chính
                Text(text = t.title, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B), lineHeight = 36.sp)
                Spacer(modifier = Modifier.height(24.dp))

                // 2. Hạn chót
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Hạn chót", fontSize = 15.sp, color = Color(0xFF64748B), modifier = Modifier.weight(1f))
                    Text(text = "${t.date}, ${t.time}", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Nhắc nhở
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Nhắc nhở", fontSize = 15.sp, color = Color(0xFF64748B), modifier = Modifier.weight(1f))
                    Text(text = if (t.remind) "Trước 15 phút" else "Tắt", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                }
                Spacer(modifier = Modifier.height(24.dp))

                // 4. Ghi chú
                Text("Ghi chú", fontSize = 15.sp, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = t.note?.takeIf { it.isNotBlank() } ?: "Không có ghi chú", fontSize = 15.sp, color = Color(0xFF1E293B), lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(24.dp))

                // 5. Danh sách Việc nhỏ (Subtasks)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Việc nhỏ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("${t.subtasks.count { it.done }}/${t.subtasks.size}", fontSize = 14.sp, color = Color(0xFF64748B))
                }
                Spacer(modifier = Modifier.height(16.dp))

                t.subtasks.forEach { sub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (sub.done) Icons.Default.CheckCircle else Icons.Outlined.Circle,
                            contentDescription = null,
                            tint = if (sub.done) Color(0xFF4361EE) else Color(0xFFCBD5E1),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = sub.title,
                            fontSize = 15.sp,
                            color = if (sub.done) Color(0xFF94A3B8) else Color(0xFF1E293B),
                            textDecoration = if (sub.done) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Outlined.Edit, contentDescription = "Sửa", tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Outlined.Delete, contentDescription = "Xóa", tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "+ Thêm việc nhỏ", color = Color(0xFF4361EE), fontSize = 15.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { /* TODO: Mở popup thêm việc nhỏ */ })

                Spacer(modifier = Modifier.height(48.dp))

                // 6. Nút Hành động
                Button(
                    onClick = {
                        // Gọi hàm cập nhật, thành công thì lùi về 1 trang
                        viewModel.markTaskAsDone(taskId) {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4361EE)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Đánh dấu hoàn thành", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        // Gọi hàm xóa, thành công thì lùi về 1 trang
                        viewModel.deleteTask(taskId) {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Xóa công việc", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        } else {
            // Trường hợp lỗi hoặc không lấy được dữ liệu
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Không tìm thấy dữ liệu", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}