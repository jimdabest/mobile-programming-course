package com.uth.weatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    WeatherAppScreen()
                }
            }
        }
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun networkStatusFlow(context: Context): Flow<Boolean> = callbackFlow {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) { trySend(true) }
        override fun onLost(network: Network) { trySend(false) }
    }
    val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(request, callback)
    trySend(isNetworkAvailable(context))
    awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherAppScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedCity by remember { mutableStateOf(WeatherApi.defaultCities.first()) }
    var weatherData by remember { mutableStateOf<OpenMeteoResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var isCacheEnabled by remember { mutableStateOf(true) }
    var isOfflineData by remember { mutableStateOf(false) }

    val customImageLoader = remember(isCacheEnabled) {
        if (isCacheEnabled) null
        else ImageLoader.Builder(context)
            .memoryCachePolicy(CachePolicy.DISABLED)
            .diskCachePolicy(CachePolicy.DISABLED)
            .build()
    }

    val isOnline by networkStatusFlow(context).collectAsState(initial = isNetworkAvailable(context))

    fun loadWeather(city: City) {
        scope.launch {
            isLoading = true
            errorMessage = null
            isOfflineData = false
            try {
                val response = WeatherApi.fetchWeather(city.lat, city.lon)
                weatherData = response
                WeatherCache.save(context, response)
                // ĐÃ XÓA DÒNG CACHEBUSTER Ở ĐÂY ĐỂ ẢNH ĐƯỢC CACHE NHANH
            } catch (e: Exception) {
                val cachedData = WeatherCache.getValidCache(context)
                if (cachedData != null) {
                    weatherData = cachedData
                    isOfflineData = true
                    errorMessage = "Mất mạng. Đang hiển thị dữ liệu lưu từ 5 phút trước."
                } else {
                    weatherData = null
                    errorMessage = "Mất kết nối mạng và dữ liệu đã quá cũ. Vui lòng kiểm tra WiFi/4G!"
                }
            } finally {
                isLoading = false
            }
        }
    }

    // Lần đầu mở app hoặc đổi thành phố
    LaunchedEffect(selectedCity) {
        loadWeather(selectedCity)
    }

    // Xử lý tự động khi mạng thay đổi
    LaunchedEffect(isOnline) {
        if (!isOnline) {
            if (weatherData != null) {
                isOfflineData = true
                errorMessage = "Mất kết nối mạng. Đang xem dữ liệu ngoại tuyến."
            } else {
                errorMessage = "Thiết bị đã mất kết nối mạng!"
            }
        } else {
            loadWeather(selectedCity)
        }
    }

    // Tự động fetch định kỳ 5 phút
    LaunchedEffect(selectedCity) {
        while (true) {
            delay(5 * 60 * 1000L)
            if (isNetworkAvailable(context)) {
                loadWeather(selectedCity)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Ứng Dụng Thời Tiết", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCity.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Chọn thành phố") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                WeatherApi.defaultCities.forEach { city ->
                    DropdownMenuItem(text = { Text(city.name) }, onClick = {
                        selectedCity = city
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = "Sử dụng bộ nhớ đệm ảnh", fontWeight = FontWeight.Medium)
                Text(text = if (isCacheEnabled) "Đang bật (Tải tức thì)" else "Đang tắt (Tải lại từ mạng)", fontSize = 12.sp, color = Color.Gray)
            }
            Switch(checked = isCacheEnabled, onCheckedChange = { isCacheEnabled = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isOfflineData) Color(0xFFFFF9C4) else MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = it, color = if (isOfflineData) Color(0xFFF57F17) else MaterialTheme.colorScheme.onErrorContainer, modifier = Modifier.padding(16.dp), fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (isLoading && weatherData == null) {
            CircularProgressIndicator()
        } else if (weatherData != null) {
            val data = weatherData!!
            val current = data.current_weather
            val iconData = getWeatherIconAndBg(current.weathercode)

            Box(modifier = Modifier.size(150.dp).clip(CircleShape).background(iconData.bgColor), contentAlignment = Alignment.Center) {
                Icon(iconData.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(80.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${current.temperature}°C", fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Text(text = iconData.description, fontSize = 20.sp, color = Color.Gray)

            // Đường link bản đồ giờ đã cố định, không còn timestamp làm mới liên tục
            val mapUrl = "https://static-maps.yandex.ru/1.x/?ll=${data.longitude},${data.latitude}&z=10&l=map&size=600,300&pt=${data.longitude},${data.latitude},pm2rdm&lang=en_US"

            var isMapLoading by remember { mutableStateOf(true) }

            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp).padding(top = 24.dp).clip(RoundedCornerShape(16.dp)).background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (isMapLoading) CircularProgressIndicator(color = Color.White)
                AsyncImage(
                    model = mapUrl,
                    imageLoader = customImageLoader ?: LocalContext.current.imageLoader,
                    contentDescription = "Bản đồ",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    onSuccess = { isMapLoading = false },
                    onError = { isMapLoading = false }
                )
            }
        }
    }
}

data class WeatherIconData(val icon: ImageVector, val bgColor: Color, val description: String)

fun getWeatherIconAndBg(code: Int): WeatherIconData {
    return when (code) {
        0 -> WeatherIconData(Icons.Default.WbSunny, Color(0xFFFFB300), "Trời quang đãng")
        1, 2, 3 -> WeatherIconData(Icons.Default.Cloud, Color(0xFF90A4AE), "Có mây")
        45, 48 -> WeatherIconData(Icons.Default.Cloud, Color(0xFF78909C), "Sương mù")
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> WeatherIconData(Icons.Default.WaterDrop, Color(0xFF42A5F5), "Có mưa")
        else -> WeatherIconData(Icons.Default.Cloud, Color.Gray, "Thời tiết khác")
    }
}