package com.uth.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Khởi tạo bộ điều hướng
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "start_screen"
                    ) {
                        // 1. MÀN HÌNH SPLASH (Màu xanh)
                        composable("start_screen") {
                            StartScreen(
                                onTimeout = {
                                    // Chuyển sang màn Onboarding, đồng thời xóa Splash khỏi lịch sử (để bấm Back không quay lại nền xanh)
                                    navController.navigate("onboarding_screen") {
                                        popUpTo("start_screen") { inclusive = true }
                                    }
                                }
                            )
                        }

                        // 2. MÀN HÌNH ONBOARDING (Giới thiệu 3 bước)
                        composable("onboarding_screen") {
                            // Biến trạng thái lưu giữ bước hiện tại (0, 1 hoặc 2)
                            var currentStep by remember { mutableIntStateOf(0) }

                            OnboardingScreen(
                                page = onboardingPages[currentStep],
                                onSkip = {
                                    // Xử lý khi bấm "Bỏ qua": Thường là vào thẳng màn hình Todo chính (sẽ làm ở các tuần sau)
                                    println("Đã bấm bỏ qua! Vào thẳng ứng dụng.")
                                },
                                onNext = {
                                    if (currentStep < onboardingPages.size - 1) {
                                        // Nếu chưa tới trang cuối -> Tăng bước lên 1 (Giao diện sẽ tự vẽ lại)
                                        currentStep++
                                    } else {
                                        // Nếu đã ở trang cuối (Nhắc đúng lúc) -> Hoàn thành Onboarding, vào app chính
                                        println("Hoàn tất giới thiệu! Vào ứng dụng.")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}