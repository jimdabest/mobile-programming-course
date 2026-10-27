package com.uth.week04.navigationlazycolumn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private object Route {
    const val ROOT = "root"
    const val LIST = "lazy_list"
    const val DETAIL = "detail"
    const val DETAIL_WITH_ID = "$DETAIL/{itemId}"
}

private const val ITEM_COUNT = 1_000_000
private val Blue = Color(0xFF2396E8)
private val PaleBlue = Color(0xFFD9F0FF)
private val Ink = Color(0xFF1C2B39)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
                    NavigationPracticeApp()
                }
            }
        }
    }
}

@Composable
private fun NavigationPracticeApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Route.ROOT) {
        composable(Route.ROOT) { RootScreen(navController) }
        composable(Route.LIST) { LazyListScreen(navController) }
        composable(
            route = Route.DETAIL_WITH_ID,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { entry ->
            DetailScreen(navController, checkNotNull(entry.arguments?.getInt("itemId")))
        }
    }
}

@Composable
private fun RootScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(PaleBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, null, tint = Blue, modifier = Modifier.size(64.dp))
            }
            Spacer(Modifier.height(26.dp))
            Text("Navigation", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Thực hành điều hướng giữa Root, LazyColumn và Detail bằng Jetpack Compose.",
                modifier = Modifier.padding(horizontal = 18.dp),
                color = Color(0xFF607080),
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = { navController.navigate(Route.LIST) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue),
            shape = RoundedCornerShape(14.dp)
        ) { Text("PUSH", fontWeight = FontWeight.Bold) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LazyListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("LazyColumn", color = Blue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to root", tint = Blue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "1,000,000 randomly generated items",
                    color = Color(0xFF607080),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(count = ITEM_COUNT, key = { it }) { index ->
                val item = itemFor(index)
                QuoteRow(item) { navController.navigate("${Route.DETAIL}/${item.id}") }
            }
        }
    }
}

@Composable
private fun QuoteRow(item: QuoteItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PaleBlue)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("%02d".format(item.id + 1), color = Blue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.width(12.dp))
            Text(item.quote, modifier = Modifier.weight(1f), color = Ink, fontSize = 14.sp, maxLines = 2)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, "Open detail", tint = Ink, modifier = Modifier.size(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailScreen(navController: NavController, itemId: Int) {
    val item = itemFor(itemId)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detail", color = Blue, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back to list", tint = Blue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))
            Text("ITEM ${item.id + 1}", color = Blue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(Modifier.height(20.dp))
            Card(colors = CardDefaults.cardColors(containerColor = PaleBlue), shape = RoundedCornerShape(20.dp)) {
                Text(
                    text = "\u201c${item.quote}\u201d",
                    modifier = Modifier.padding(28.dp),
                    color = Ink,
                    fontSize = 27.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 38.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { navController.popBackStack(Route.ROOT, inclusive = false) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue),
                shape = RoundedCornerShape(14.dp)
            ) { Text("BACK TO ROOT", fontWeight = FontWeight.Bold) }
        }
    }
}

private data class QuoteItem(val id: Int, val quote: String)

private val quotes = arrayOf(
    "The only way to do great work is to love what you do.",
    "Small progress is still progress.",
    "Your future is created by what you do today.",
    "Learning never exhausts the mind.",
    "Believe you can and you are halfway there.",
    "Great things start with a single step."
)

/* A stable pseudo-random choice avoids allocating one million objects up front. */
private fun itemFor(id: Int): QuoteItem {
    val quoteIndex = ((id.toLong() * 1_103_515_245L + 12_345L) and 0x7fffffffL).toInt() % quotes.size
    return QuoteItem(id, quotes[quoteIndex])
}
