package com.uth.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        stepIndex = 2, // Đã sửa lại thành 2
        title = "Nhắc đúng lúc",
        description = "Bật thông báo dể UTH Todo nhắc bạn trước hạn chót.",
        buttonText = "Bắt đầu ngay",
    )
)

@Composable
fun OnboardingScreen(
    page: OnboardingPage,
    onSkip: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF1E3A8A), fontWeight = FontWeight.ExtraBold)) { append("UTH") }
                    withStyle(style = SpanStyle(color = Color(0xFF3B82F6), fontWeight = FontWeight.Medium)) { append("Todo") }
                },
                fontSize = 16.sp
            )
            Text(
                text = "Bỏ qua",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSkip() },
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.85f).fillMaxHeight().background(Color(0xFFF1F5F9), shape = RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (page.stepIndex) {
                    0 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            repeat(3) { index ->
                                Row(
                                    modifier = Modifier.width(160.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    val isChecked = index == 0
                                    Box(
                                        modifier = Modifier.size(16.dp).background(color = if (isChecked) Color(0xFF2563EB) else Color.Transparent, shape = RoundedCornerShape(8.dp))
                                            .border(width = 2.dp, color = if (isChecked) Color(0xFF2563EB) else Color(0xFFCBD5E1), shape = RoundedCornerShape(8.dp))
                                    )
                                    Box(modifier = Modifier.height(8.dp).weight(1f).background(Color(0xFFE2E8F0), RoundedCornerShape(4.dp)))
                                }
                            }
                        }
                    }
                    1 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                modifier = Modifier.width(160.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFF3B82F6), RoundedCornerShape(4.dp)))
                                Text("Học tập", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            }
                            Row(
                                modifier = Modifier.width(160.dp).background(Color.White, RoundedCornerShape(12.dp)).padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(modifier = Modifier.size(8.dp).background(Color(0xFFF59E0B), RoundedCornerShape(4.dp)))
                                Text("Cuộc sống", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                            }
                        }
                    }
                    2 -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Box(modifier = Modifier.size(64.dp).background(Color.White, RoundedCornerShape(32.dp)), contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Notifications,
                                    contentDescription = "Bell",
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Box(modifier = Modifier.background(Color.White, RoundedCornerShape(16.dp)).padding(horizontal = 16.dp, vertical = 10.dp)) {
                                Text("Ôn bài Toán lúc 19:00", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                            }
                        }
                    }
                }
            }
        }

        Text(text = page.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), lineHeight = 32.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = page.description, fontSize = 14.sp, color = Color(0xFF64748B), lineHeight = 22.sp)
        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) { index ->
                val isSelected = index == page.stepIndex
                Box(modifier = Modifier.height(6.dp).width(if (isSelected) 24.dp else 6.dp).background(color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFE2E8F0), shape = RoundedCornerShape(3.dp)))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNext,
            shape = RoundedCornerShape(10.dp),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(text = page.buttonText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OnboardingPreview() {
    OnboardingScreen(page = onboardingPages[0], onSkip = {}, onNext = {})
}