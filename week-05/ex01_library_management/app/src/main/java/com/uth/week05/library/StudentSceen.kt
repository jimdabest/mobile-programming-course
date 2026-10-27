package com.uth.week05.library

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StudentScreen(manager: LibraryManager) {
    var cccd by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var editingStudent by remember { mutableStateOf<Student?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quản lý Sinh viên", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cccd,
            onValueChange = { cccd = it },
            label = { Text("Số CCCD") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (editingStudent == null) {
                    manager.addStudent(cccd, name)
                } else {
                    manager.updateStudent(editingStudent!!, cccd, name)
                    editingStudent = null
                }
                cccd = ""
                name = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingStudent == null) "Thêm Sinh viên" else "Cập nhật Sinh viên")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(manager.students) { student ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = student.name, fontWeight = FontWeight.Bold)
                            Text(text = "CCCD: ${student.cccd}", fontSize = 14.sp)
                        }
                        Row {
                            TextButton(onClick = {
                                cccd = student.cccd
                                name = student.name
                                editingStudent = student
                            }) {
                                Text("Sửa")
                            }
                            TextButton(onClick = { manager.deleteStudent(student) }) {
                                Text("Xóa", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}