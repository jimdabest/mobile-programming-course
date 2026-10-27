package com.uth.week05.library

import java.util.UUID

abstract class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String
) {
    var isBorrowed: Boolean = false
        private set

    fun markAsBorrowed() { isBorrowed = true }
    fun markAsReturned() { isBorrowed = false }

    abstract fun getDisplayInfo(): String
}

class Book(title: String, val author: String) : Document(title = title) {

    override fun getDisplayInfo(): String {
        return "[Sách] $title - Tác giả: $author"
    }
}

class Magazine(title: String, val issueNumber: Int) : Document(title = title) {

    override fun getDisplayInfo(): String {
        return "[Tạp chí] $title - Số phát hành: $issueNumber"
    }
}

data class Student(
    val cccd: String,
    val name: String
)