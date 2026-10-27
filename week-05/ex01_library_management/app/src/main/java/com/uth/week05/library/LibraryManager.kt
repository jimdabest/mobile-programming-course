package com.uth.week05.library

import androidx.compose.runtime.mutableStateListOf

class LibraryManager {
    val students = mutableStateListOf<Student>()
    val documents = mutableStateListOf<Document>()

    init {
        students.add(Student(cccd = "079204000123", name = "Phạm Nguyễn"))

        // Thêm cả Sách và Tạp chí vào cùng một danh sách
        documents.add(Book(title = "Lập trình Android với Compose", author = "UTH"))
        documents.add(Magazine(title = "Tạp chí Khoa học Máy tính", issueNumber = 45))
    }

    // Các hàm quản lý Sinh viên
    fun addStudent(cccd: String, name: String) {
        if (cccd.isNotBlank() && name.isNotBlank() && students.none { it.cccd == cccd }) {
            students.add(Student(cccd = cccd, name = name))
        }
    }
    fun deleteStudent(student: Student) { students.remove(student) }

    // Các hàm quản lý Tài liệu
    fun addDocument(doc: Document) {
        documents.add(doc)
    }
    fun deleteDocument(doc: Document) {
        documents.remove(doc)
    }
}