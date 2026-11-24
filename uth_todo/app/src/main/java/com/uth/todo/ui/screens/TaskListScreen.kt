package com.uth.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uth.todo.model.TaskResponse
import com.uth.todo.viewmodel.TodoViewModel

@Composable
fun TaskListScreen(navController: NavController, viewModel: TodoViewModel = viewModel()) {
    // 1. Lắng nghe dữ liệu
    val allTasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPreview = LocalInspectionMode.current

    // Biến trạng thái cho Bộ lọc & Tìm kiếm
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Tất cả") }
    val tabs = listOf("Tất cả", "Học tập", "Cuộc sống")

    // Dữ liệu Preview giả lập
    val displayTasks = if (isPreview) {
        listOf(
            TaskResponse(1, "Ôn chương 3 - Cấu trúc dữ liệu", "", "study", "", "14:00", "", false, false, emptyList()),
            TaskResponse(2, "Đi chợ mua đồ cho tuần", "", "life", "", "17:30", "", false, false, emptyList()),
            TaskResponse(3, "Tập thể dục 30 phút", "", "life", "", "06:30", "", false, true, emptyList())
        )
    } else allTasks

    // 2. Logic Lọc dữ liệu (Theo chữ và theo tab)
    val filteredTasks = displayTasks.filter { task ->
        val matchesSearch = task.title.contains(searchQuery, ignoreCase = true)
        val matchesTab = when (selectedTab) {
            "Học tập" -> task.category == "study"
            "Cuộc sống" -> task.category == "life"
            else -> true
        }
        matchesSearch && matchesTab
    }

    // Phân tách thành 2 nhóm
    val todoTasks = filteredTasks.filter { !it.done }
    val doneTasks = filteredTasks.filter { it.done }

    // 3. Giao diện
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        bottomBar = { HomeBottomBar(navController, currentRoute = "task_list_screen") }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Danh sách", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1E293B))
                Text("${todoTasks.size} việc chưa xong", fontSize = 13.sp, color = Color(0xFF64748B))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Thanh tìm kiếm
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tìm công việc", color = Color(0xFF94A3B8)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF94A3B8)) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF3B82F6)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Thanh Tab lọc (Tất cả, Học tập, Cuộc sống)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Button(
                        onClick = { selectedTab = tab },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF1E293B) else Color.White,
                            contentColor = if (isSelected) Color.White else Color(0xFF64748B)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        elevation = if (!isSelected) ButtonDefaults.buttonElevation(defaultElevation = 0.dp) else ButtonDefaults.buttonElevation()
                    ) {
                        Text(tab, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Danh sách hiển thị
            if (isLoading && !isPreview) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4361EE))
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    // Danh sách CẦN LÀM
                    if (todoTasks.isNotEmpty()) {
                        item {
                            Text("CẦN LÀM", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.padding(bottom = 12.dp))
                        }
                        items(todoTasks) { task ->
                            // Tái sử dụng TaskItem bên HomeScreen.kt
                            TaskItem(
                                taskId = task.id,
                                title = task.title,
                                category = if (task.category == "study") "Học tập" else "Cuộc sống",
                                time = task.time,
                                isDone = task.done,
                                navController = navController
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Danh sách ĐÃ XONG
                    if (doneTasks.isNotEmpty()) {
                        item {
                            Text("ĐÃ XONG", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8), modifier = Modifier.padding(bottom = 12.dp))
                        }
                        items(doneTasks) { task ->
                            TaskItem(
                                taskId = task.id,
                                title = task.title,
                                category = if (task.category == "study") "Học tập" else "Cuộc sống",
                                time = task.time,
                                isDone = task.done,
                                navController = navController
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) } // Tránh bị chìm dưới BottomBar
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskListScreenPreview() {
    TaskListScreen(navController = rememberNavController())
}