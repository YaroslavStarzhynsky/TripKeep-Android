package pl.yskp.tripkeep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
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
import pl.yskp.tripkeep.viewmodel.PlannerViewModel
import java.util.concurrent.TimeUnit

@Composable
fun PlannerScreen(
    onBackClick: () -> Unit,
    onAddTripClick: () -> Unit,
    onHomeClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onProfileClick: () -> Unit,
    onRealizedClick: (Long) -> Unit,
    viewModel: PlannerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            PlannerTopBar(
                onBackClick = onBackClick,
                onProfileClick = onProfileClick,
                avatarUri = uiState.avatarUri
            )
        },
        bottomBar = {
            HomeBottomBar(
                currentScreen = TripKeepScreen.Planner,
                onHomeClick = onHomeClick,
                onGalleryClick = onGalleryClick,
                onPlansClick = {},
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
            Spacer(modifier = Modifier.height(16.dp))
            
            // Add Trip Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { onAddTripClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Dodaj podróż",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(uiState.plannedTrips) { trip ->
                    PlannerTripCard(
                        trip = trip,
                        onCheckClick = {
                            viewModel.markAsRealized(trip.tripId)
                            onRealizedClick(trip.tripId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlannerTopBar(
    onBackClick: () -> Unit,
    onProfileClick: () -> Unit,
    avatarUri: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
        }
        
        Text(
            text = "Plany",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

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
fun PlannerTripCard(
    trip: TripEntity,
    onCheckClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Trip Image Placeholder/Image
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFEEEEEE))
            ) {
                if (trip.mainImageUri.isNotEmpty()) {
                    AsyncImage(
                        model = trip.mainImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.Center),
                        tint = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = trip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = trip.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                
                val daysLeft = calculateDaysLeft(trip.dateTimestamp)
                Text(
                    text = if (daysLeft >= 0) "za $daysLeft dni" else "oczekuje",
                    color = TripKeepOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = onCheckClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF1F1F1), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Mark as realized",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

private fun calculateDaysLeft(dateTimestamp: Long): Long {
    val diff = dateTimestamp - System.currentTimeMillis()
    return if (diff > 0) {
        TimeUnit.MILLISECONDS.toDays(diff)
    } else {
        0
    }
}
