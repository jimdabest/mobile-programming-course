package com.uth.todo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uth.todo.model.TaskResponse
import com.uth.todo.network.RetrofitTodoClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<TaskResponse>>(emptyList())
    val tasks: StateFlow<List<TaskResponse>> = _tasks

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading
    // Quản lý dữ liệu cho màn hình chi tiết
    private val _selectedTask = MutableStateFlow<TaskResponse?>(null)
    val selectedTask: StateFlow<TaskResponse?> = _selectedTask

    private val _isDetailLoading = MutableStateFlow(false)
    val isDetailLoading: StateFlow<Boolean> = _isDetailLoading
    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _tasks.value = RetrofitTodoClient.apiService.getTasks()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Hàm gọi API lấy chi tiết 1 Task
    fun fetchTaskDetail(taskId: Int) {
        viewModelScope.launch {
            try {
                _isDetailLoading.value = true
                _selectedTask.value = RetrofitTodoClient.apiService.getTaskDetail(taskId)
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedTask.value = null
            } finally {
                _isDetailLoading.value = false
            }
        }
    }
    // Hàm gọi API Đánh dấu hoàn thành
    fun markTaskAsDone(taskId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isDetailLoading.value = true
                val updateData = mapOf("done" to true)
                RetrofitTodoClient.apiService.updateTaskStatus(taskId, updateData)

                fetchTasks() // Tải lại danh sách mới nhất
                onSuccess()  // Gọi hàm điều hướng về trang trước
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isDetailLoading.value = false
            }
        }
    }

    // Hàm gọi API Xóa công việc
    fun deleteTask(taskId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isDetailLoading.value = true
                RetrofitTodoClient.apiService.deleteTask(taskId)

                fetchTasks() // Tải lại danh sách sau khi đã xóa
                onSuccess()  // Gọi hàm điều hướng về trang trước
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isDetailLoading.value = false
            }
        }
    }
}