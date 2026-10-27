package com.uth.week04.uicomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage

// 1. Định nghĩa danh sách đường dẫn (Routes) cho các màn hình
sealed class Screen(val route: String) {
    object Ready : Screen("ready")
    object ComponentList : Screen("component_list")
    object TextDetail : Screen("text_detail")
    object ImageDetail : Screen("image_detail")
    object TextFieldDetail : Screen("textfield_detail")
    object PasswordFieldDetail : Screen("password_detail")
    object RowLayoutDetail : Screen("row_layout")
    object ColumnLayoutDetail : Screen("column_layout")
    object SelfResearchDetail : Screen("self_research")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()

                    // Bộ điều hướng trung tâm
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Ready.route
                    ) {
                        composable(Screen.Ready.route) { ReadyScreen(navController) }
                        composable(Screen.ComponentList.route) { ComponentListScreen(navController) }
                        composable(Screen.TextDetail.route) { TextDetailScreen(navController) }
                        composable(Screen.ImageDetail.route) { ImageDetailScreen(navController) }
                        composable(Screen.TextFieldDetail.route) { TextFieldDetailScreen(navController) }
                        composable(Screen.PasswordFieldDetail.route) { PasswordFieldDetailScreen(navController) }
                        composable(Screen.RowLayoutDetail.route) { RowLayoutScreen(navController) }
                        composable(Screen.ColumnLayoutDetail.route) { ColumnLayoutScreen(navController) }
                        composable(Screen.SelfResearchDetail.route) { SelfResearchScreen(navController) }
                    }
                }
            }
        }
    }
}

// ==========================================
// MÀN 1: I'M READY (Màn hình giới thiệu)
// ==========================================
@Composable
fun ReadyScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.avt_ai),
                contentDescription = "Jetpack Compose Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "Jetpack Compose",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Button(
            onClick = { navController.navigate(Screen.ComponentList.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2997FF))
        ) {
            Text(
                text = "I'm ready",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// ==========================================
// MÀN 2: DANH SÁCH THÀNH PHẦN UI (UI Components List)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "UI Components List",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2997FF),
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CategoryHeader("Display")
            ComponentCard("Text", "Displays text") {
                navController.navigate(Screen.TextDetail.route)
            }
            ComponentCard("Image", "Displays an image") {
                navController.navigate(Screen.ImageDetail.route)
            }

            Spacer(modifier = Modifier.height(10.dp))
            CategoryHeader("Input")
            ComponentCard("TextField", "Input field for text") {
                navController.navigate(Screen.TextFieldDetail.route)
            }
            ComponentCard("PasswordField", "Input field for passwords") {
                navController.navigate(Screen.PasswordFieldDetail.route)
            }

            Spacer(modifier = Modifier.height(10.dp))
            CategoryHeader("Layout")
            ComponentCard("Column", "Arranges elements vertically") {
                navController.navigate(Screen.ColumnLayoutDetail.route)
            }
            ComponentCard("Row", "Arranges elements horizontally") {
                navController.navigate(Screen.RowLayoutDetail.route)
            }

        }
    }
}

// ==========================================
// MÀN 3: TEXT DETAIL
// ==========================================
@Composable
fun TextDetailScreen(navController: NavController) {
    DetailScaffold(title = "Text Detail", navController = navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 30.sp, color = Color.Black)) { append("The ") }
                withStyle(style = SpanStyle(fontSize = 30.sp, textDecoration = TextDecoration.LineThrough, color = Color.Black)) { append("quick") }
                withStyle(style = SpanStyle(fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF0000))) { append(" Brown\n") }
                withStyle(style = SpanStyle(fontSize = 30.sp, color = Color.Black)) { append("fox ") }
                withStyle(style = SpanStyle(fontSize = 30.sp, letterSpacing = 6.sp, color = Color.Black)) { append("jumps ") }
                withStyle(style = SpanStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic, color = Color.Black)) { append("over\n") }
                withStyle(style = SpanStyle(fontSize = 30.sp, textDecoration = TextDecoration.Underline, color = Color.Black)) { append("the ") }
                withStyle(style = SpanStyle(fontSize = 28.sp, fontStyle = FontStyle.Italic, color = Color.Black)) { append("lazy") }
                withStyle(style = SpanStyle(fontSize = 30.sp, color = Color.Black)) { append(" dog.") }
            }
            Column {
                Text(text = styledText, lineHeight = 46.sp)
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE5E7EB))
            }
        }
    }
}

// ==========================================
// MÀN 4: IMAGES DETAIL
// ==========================================
@Composable
fun ImageDetailScreen(navController: NavController) {
    val imageUrl = "https://giaothongvantaitphcm.edu.vn/wp-content/uploads/2025/01/Logo-GTVT.png"
    DetailScaffold(title = "Images", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Online Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F4F6)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = imageUrl,
                fontSize = 11.sp,
                color = Color.DarkGray,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.avt_ai),
                contentDescription = "In App Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF3F4F6)),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "In app", fontSize = 12.sp, color = Color.DarkGray)
        }
    }
}

// ==========================================
// MÀN 5: TEXTFIELD DETAIL
// ==========================================
@Composable
fun TextFieldDetailScreen(navController: NavController) {
    var text by remember { mutableStateOf("") }
    DetailScaffold(title = "TextField", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Thông tin nhập", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (text.isEmpty()) "Tự động cập nhật dữ liệu theo textfield" else text,
                color = Color(0xFFDC2626),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// MÀN 6: PASSWORD FIELD DETAIL
// ==========================================
@Composable
fun PasswordFieldDetailScreen(navController: NavController) {
    var password by remember { mutableStateOf("") }
    DetailScaffold(title = "PasswordField", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Nhập mật khẩu", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Độ dài mật khẩu: ${password.length} ký tự",
                color = Color(0xFF1E60BD),
                fontSize = 14.sp
            )
        }
    }
}

// ==========================================
// MÀN 7: ROW LAYOUT DETAIL
// ==========================================
@Composable
fun RowLayoutScreen(navController: NavController) {
    DetailScaffold(title = "Row Layout", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .background(Color(0xFFA5C4FA), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .background(Color(0xFF4A89F3), RoundedCornerShape(10.dp))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .background(Color(0xFFA5C4FA), RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// MÀN 8: COLUMN LAYOUT DETAIL
// ==========================================
@Composable
fun ColumnLayoutScreen(navController: NavController) {
    DetailScaffold(title = "Column Layout", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .background(
                            if (index % 2 == 0) Color(0xFFA5C4FA) else Color(0xFF4A89F3),
                            RoundedCornerShape(12.dp)
                        )
                )
            }
        }
    }
}

// ==========================================
// MÀN 9: TỰ TÌM HIỂU (Tất cả UI Components cơ bản - Slide 64)
// ==========================================
@Composable
fun SelfResearchScreen(navController: NavController) {
    DetailScaffold(title = "All Basic UI Components", navController = navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            CategoryHeader("Display")
            ComponentCard("Text", "Displays text") {}
            ComponentCard("Image", "Displays an image") {}

            Spacer(modifier = Modifier.height(8.dp))
            CategoryHeader("Input")
            ComponentCard("TextField", "Input field for text") {}
            ComponentCard("PasswordField", "Input field for passwords") {}
            ComponentCard("Toggle", "On/off switch") {}
            ComponentCard("Slider", "Selects a value from a range") {}
            ComponentCard("DatePicker", "Selects a date and time") {}

            Spacer(modifier = Modifier.height(8.dp))
            CategoryHeader("Selection")
            ComponentCard("Button", "Triggers an action when tapped") {}
            ComponentCard("Checkbox", "Selects one or multiple options") {}
            ComponentCard("RadioButton", "Selects a single option") {}
            ComponentCard("Picker", "Selects a value from a list") {}

            Spacer(modifier = Modifier.height(8.dp))
            CategoryHeader("Navigation")
            ComponentCard("NavigationView", "Navigation container with title and back button") {}
            ComponentCard("TabBar", "Switches between main sections") {}
            ComponentCard("Menu", "Shows a menu with multiple actions") {}

            Spacer(modifier = Modifier.height(8.dp))
            CategoryHeader("Content")
            ComponentCard("List", "Displays a list of items") {}
            ComponentCard("Card", "Container with content and style") {}
            ComponentCard("Divider", "Separates content into sections") {}
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ==========================================
// CÁC COMPONENT DÙNG CHUNG (Helper Widgets)
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScaffold(
    title: String,
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = title,
                        color = Color(0xFF2997FF),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2997FF)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}

@Composable
fun CategoryHeader(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color(0xFF1F2937),
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@Composable
fun ComponentCard(title: String, subtitle: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(Color(0xFFC7E2FF), shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color(0xFF475569)
            )
        }
    }
}
