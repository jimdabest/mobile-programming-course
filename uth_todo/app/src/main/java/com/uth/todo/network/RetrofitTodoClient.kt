package com.uth.todo.network

import com.uth.todo.model.TaskResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface TodoApiService {
    // 1. Dùng cho Flow Trang chủ & Flow Danh sách
    @GET("tasks")
    suspend fun getTasks(
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): List<TaskResponse>

    // 2. Dùng cho Flow Chi tiết
    @GET("tasks/{id}")
    suspend fun getTaskDetail(
        @Path("id") taskId: Int,
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): TaskResponse

    // Thêm đoạn code này vào bên trong interface TodoApiService
    // 3. API Cập nhật một phần (Đánh dấu hoàn thành)
    @PATCH("tasks/{id}")
    suspend fun updateTaskStatus(
        @Path("id") taskId: Int,
        @Body body: Map<String, Boolean>, // Gửi lên cục JSON dạng {"done": true}
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): TaskResponse

    // 4. API Xóa công việc
    @DELETE("tasks/{id}")
    suspend fun deleteTask(
        @Path("id") taskId: Int,
        @Header("Authorization") token: String = "Bearer TuanTruong-uth-student"
    ): Response<Unit> // Dùng Response<Unit> vì API Delete thường không trả về data
}

object RetrofitTodoClient {
    private const val BASE_URL = "https://share.tryapps.store/mock/teach/"

    val apiService: TodoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApiService::class.java)
    }
}