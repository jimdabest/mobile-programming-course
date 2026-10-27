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
fun BorrowScreen(manager: LibraryManager) {
    // Đổi tên biến từ cccd thành searchQuery để dùng chung cho cả tên và CCCD
    var searchQuery by remember { mutableStateOf("") }
    val selectedBooks = remember { mutableStateListOf<Book>() }
    var resultMessage by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Hệ thống Quản lý Thư viện", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Cập nhật giao diện ô nhập
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Nhập CCCD hoặc Tên Sinh viên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Danh sách sách hiện có", fontWeight = FontWeight.SemiBold)

        LazyColumn(modifier = Modifier.weight(1f).padding(top = 8.dp)) {
            items(manager.books) { book ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = selectedBooks.contains(book),
                        onCheckedChange = { isChecked ->
                            if (isChecked) selectedBooks.add(book)
                            else selectedBooks.remove(book)
                        }
                    )
                    Text("${book.title} (Tác giả: ${book.author})")
                }
            }
        }

        Button(
            onClick = {
                // LOGIC MỚI: Tìm sinh viên có CCCD khớp chính xác HOẶC Tên có chứa từ khóa (không phân biệt hoa/thường)
                val student = manager.students.find {
                    it.cccd == searchQuery || it.name.contains(searchQuery, ignoreCase = true)
                }

                if (student == null) {
                    resultMessage = "Lỗi: Không tìm thấy sinh viên này!"
                } else if (selectedBooks.isEmpty()) {
                    resultMessage = "Vui lòng chọn ít nhất 1 quyển sách!"
                } else {
                    val bookTitles = selectedBooks.joinToString(", ") { it.title }
                    resultMessage = "Sinh viên: ${student.name} (CCCD: ${student.cccd})\nĐã mượn: $bookTitles"
                    selectedBooks.clear()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Thêm (Ghi nhận mượn)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (resultMessage.isNotEmpty()) {
            Text(text = resultMessage, color = Color(0xFF2563EB), fontWeight = FontWeight.Medium)
        }
    }
}