package pl.yskp.tripkeep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.navigation.TripKeepScreen
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    onMenuClick: () -> Unit,
    onProfileClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onPlansClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val memories by viewModel.memories.collectAsState()
    val plans by viewModel.plans.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                onMenuClick = onMenuClick,
                onProfileClick = onProfileClick,
                avatarUri = uiState.avatarUri
            )
        },
        bottomBar = {
            HomeBottomBar(
                currentScreen = TripKeepScreen.Home,
                onHomeClick = {},
                onGalleryClick = onGalleryClick,
                onPlansClick = onPlansClick,
                onProfileClick = onProfileClick
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Hej, ${uiState.userName.ifEmpty { "Podróżniku" }}",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color.Black
                )
            )
            Text(
                text = "Twój Pamiętnik Podróży",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(title = "Ostatnie podróże")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (memories.isEmpty()) {
                EmptyStateCard(text = "Brak wspomnień. Dodaj swoją pierwszą wyprawę!")
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(memories) { trip ->
                        MemoryCard(trip)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader(title = "Zaplanowane podróże")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (plans.isEmpty()) {
                EmptyStateCard(text = "Nic jeszcze nie zaplanowano.")
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    plans.take(2).forEach { trip ->
                        PlanItem(trip)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(onMenuClick: () -> Unit, onProfileClick: () -> Unit, avatarUri: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onMenuClick) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu", tint = Color.Gray)
        }
        Text(text = "Home", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
        IconButton(
            onClick = onProfileClick,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                if (avatarUri.isNotEmpty()) {
                    AsyncImage(
                        model = avatarUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(
    currentScreen: TripKeepScreen,
    onHomeClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onPlansClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 24.dp)
            .height(70.dp)
            .background(Color.Black, RoundedCornerShape(35.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(Icons.Default.Home, contentDescription = null, tint = if (currentScreen == TripKeepScreen.Home) TripKeepOrange else Color.White, modifier = Modifier.size(28.dp))
            }
            IconButton(onClick = onGalleryClick) {
                Icon(Icons.Default.Image, contentDescription = null, tint = if (currentScreen == TripKeepScreen.Gallery) TripKeepOrange else Color.White, modifier = Modifier.size(28.dp))
            }
            IconButton(onClick = onPlansClick) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = if (currentScreen == TripKeepScreen.Planner) TripKeepOrange else Color.White, modifier = Modifier.size(28.dp))
            }
            IconButton(onClick = onProfileClick) {
                Icon(Icons.Default.Person, contentDescription = null, tint = if (currentScreen == TripKeepScreen.Profile) TripKeepOrange else Color.White, modifier = Modifier.size(28.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )
        Text(
            text = "Zobacz wszystkie",
            style = MaterialTheme.typography.bodySmall.copy(
                color = TripKeepOrange,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun MemoryCard(trip: TripEntity) {
    Card(
        modifier = Modifier.size(width = 160.dp, height = 220.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box {
            AsyncImage(
                model = trip.mainImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(text = trip.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = trip.location, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun PlanItem(trip: TripEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = trip.mainImageUri,
                contentDescription = null,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = trip.title, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = trip.location, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EmptyStateCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.5f))
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = text, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
