package com.uth.todo.model

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    val id: Int,
    val title: String,
    val note: String?,
    val category: String,
    val date: String,
    val time: String,
    val priority: String,
    val remind: Boolean,
    val done: Boolean,
    val subtasks: List<SubTask>
)

data class SubTask(
    val title: String,
    val done: Boolean
)