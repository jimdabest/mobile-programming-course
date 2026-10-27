package com.uth.week05.dataflow

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TopHeader(navController: NavController, showBackButton: Boolean = true) {
    Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        if (showBackButton) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(28.dp)
                    .clickable { navController.popBackStack() },
                tint = Color(0xFF3B82F6)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("UTH", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
            Text("UNIVERSITY OF TRANSPORT", fontSize = 8.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            Text("HOCHIMINH CITY", fontSize = 8.sp, color = Color.Red, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("SmartTasks", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
        }
    }
}