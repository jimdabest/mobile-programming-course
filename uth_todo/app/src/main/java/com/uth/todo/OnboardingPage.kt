package com.uth.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uth.todo.ui.theme.UTHTodoTheme

data class OnboardingPage(
    val stepIndex: Int,
    val title: String,
    val description: String,
    val buttonText: String,
)

val onboardingPages = listOf(
    OnboardingPage(
        stepIndex = 0,
        title = "Mọi việc, gọn trong một chỗ",
        description = "Ghi lại bài tập, việc nhà, lịch hẹn. Không còn quên việc nào.",
        buttonText = "Tiếp tục",
    ),

    OnboardingPage(
        stepIndex = 1,
        title = "Chia theo Học tập và Cuộc sống",
        description = "Mỗi việc có nhóm riêng, nhìn là biết hôm nay cần lo gì.",
        buttonText = "Tiếp tục",
    ),

    OnboardingPage(
        stepIndex = 0,
        title = "Nhắc đúng lúc",
        description = "Bật thông báo dể UTH Todo nhắc bạn trước hạn chót.",
        buttonText = "Bắt đầu ngay",
    ),
)