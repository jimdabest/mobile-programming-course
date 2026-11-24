package com.uth.e_learning_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ==========================================
// 1. KOTLIN SERIALIZATION: Định nghĩa Model
// ==========================================
@Serializable
data class RandomUserResponse(
    @SerialName("results") val results: List<UserResult>
)

@Serializable
data class UserResult(
    @SerialName("name") val name: UserName,
    @SerialName("email") val email: String,
    @SerialName("picture") val picture: UserPicture
)

@Serializable
data class UserName(
    @SerialName("first") val first: String,
    @SerialName("last") val last: String
)

@Serializable
data class UserPicture(
    @SerialName("large") val largeUrl: String
)

// ==========================================
// 2. MAIN ACTIVITY & KTOR CLIENT
// ==========================================
class MainActivity : ComponentActivity() {

    // Khởi tạo Ktor Client
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // Bỏ qua các data thừa của API
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RandomUserScreen(client = client)
                }
            }
        }
    }
}

// ==========================================
// 3. GIAO DIỆN JETPACK COMPOSE
// ==========================================
@Composable
fun RandomUserScreen(client: HttpClient) {
    // Các biến trạng thái (State)
    var user by remember { mutableStateOf<UserResult?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Thẻ hiển thị thông tin
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // KIỂM TRA TRẠNG THÁI HIỂN THỊ
                if (errorMessage != null) {
                    // Nếu có lỗi -> In ra màn hình màu đỏ
                    Text(
                        text = "LỖI API:\n$errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                } else if (user != null) {
                    // Nếu có dữ liệu -> Hiện Avatar và Thông tin
                    AsyncImage(
                        model = user!!.picture.largeUrl,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${user!!.name.first} ${user!!.name.last}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user!!.email,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                } else {
                    // Trạng thái ban đầu
                    Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                        Text("Chưa có hồ sơ")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Nút bấm kích hoạt API
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null // Reset lỗi mỗi lần bấm
                    try {
                        // Ktor gọi mạng
                        val response: RandomUserResponse =
                            client.get("https://randomuser.me/api/").body()

                        // Cập nhật giao diện
                        user = response.results.firstOrNull()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        errorMessage = e.message // Bắt lỗi để hiện ra UI
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Tìm Nhân Sự Ngẫu Nhiên", fontSize = 16.sp)
            }
        }
    }
}