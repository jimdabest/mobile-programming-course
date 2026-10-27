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
fun BookScreen(manager: LibraryManager) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var editingBook by remember { mutableStateOf<Book?>(null) } // Lưu trạng thái cuốn sách đang sửa

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quản lý Sách", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Khu vực nhập liệu
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Tên sách") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Tác giả") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Nút Thêm / Cập nhật
        Button(
            onClick = {
                if (editingBook == null) {
                    manager.addBook(title, author)
                } else {
                    manager.updateBook(editingBook!!, title, author)
                    editingBook = null // Reset trạng thái sau khi sửa
                }
                title = ""
                author = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (editingBook == null) "Thêm Sách" else "Cập nhật Sách")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Danh sách hiển thị
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(manager.books) { book ->
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
                            Text(text = book.title, fontWeight = FontWeight.Bold)
                            Text(text = book.author, fontSize = 14.sp)
                        }
                        // Dùng TextButton thay cho Icon để tránh lỗi thiếu thư viện
                        Row {
                            TextButton(onClick = {
                                title = book.title
                                author = book.author
                                editingBook = book
                            }) {
                                Text("Sửa")
                            }
                            TextButton(onClick = { manager.deleteBook(book) }) {
                                Text("Xóa", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}