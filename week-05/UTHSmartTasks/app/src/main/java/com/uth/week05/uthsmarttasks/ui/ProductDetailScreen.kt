package com.uth.week05.uthsmarttasks.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.uth.week05.uthsmarttasks.model.ProductResponse
import com.uth.week05.uthsmarttasks.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductViewModel : ViewModel() {
    private val _product = MutableStateFlow<ProductResponse?>(null)
    val product: StateFlow<ProductResponse?> = _product

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    init { getProduct() }

    // Cung cấp hàm công khai để UI có thể kích hoạt tải lại
    fun refreshData() {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            try {
                // Khi bật biến này, UI sẽ tự động hiện vòng xoay loading
                _isLoading.value = true
                _errorMessage.value = ""
                _product.value = RetrofitClient.apiService.getProductDetail()
            } catch (e: Exception) {
                e.printStackTrace()
                android.util.Log.e("API_ERROR", "Lỗi: ", e)
                _errorMessage.value = e.message ?: "Lỗi không xác định"
            } finally {
                // Tắt vòng xoay loading khi đã có dữ liệu hoặc có lỗi
                _isLoading.value = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: ProductViewModel = viewModel()) {
    val isPreview = LocalInspectionMode.current

    val product = if (isPreview) {
        ProductResponse("1", "Intelligent Soft Car", 106.89, "Mô tả sản phẩm mẫu.", "")
    } else {
        viewModel.product.collectAsState().value
    }

    val isLoading = if (isPreview) false else viewModel.isLoading.collectAsState().value
    val errorMessage = if (isPreview) "" else viewModel.errorMessage.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product detail", color = Color(0xFF00BCD4), fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { /* Xử lý back */ }) {
                        Box(
                            modifier = Modifier.size(32.dp).background(Color(0xFF00BCD4), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                },
                // Thêm actions vào TopAppBar để vẽ nút góc trên bên phải
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Tải lại",
                            tint = Color(0xFF00BCD4),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF00BCD4))
            }
        } else if (product != null) {
            val formatter = NumberFormat.getInstance(Locale.forLanguageTag("vi-VN"))
            val formattedPrice = "${formatter.format(product.price ?: 0.0)}$"

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth().height(300.dp).background(Color(0xFFF44336), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!isPreview) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = "Product Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(text = product.name ?: "", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.Black, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Giá: ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = formattedPrice, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp)).padding(16.dp)) {
                    Text(text = product.description ?: "", fontSize = 13.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        } else if (errorMessage.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(text = "Lỗi tải dữ liệu: $errorMessage", color = Color.Red)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    ProductDetailScreen()
}