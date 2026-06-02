package pl.yskp.tripkeep.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.navigation.TripKeepScreen
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.GalleryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GalleryScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onPlansClick: () -> Unit,
    onProfileClick: () -> Unit,
    onTripClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: GalleryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val memories by viewModel.memories.collectAsState()

    Scaffold(
        topBar = {
            GalleryTopBar(onBackClick = onBackClick)
        },
        bottomBar = {
            HomeBottomBar(
                currentScreen = TripKeepScreen.Gallery,
                onHomeClick = onHomeClick,
                onGalleryClick = {},
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
            Spacer(modifier = Modifier.height(16.dp))

            // Add Trip Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(6.dp, RoundedCornerShape(24.dp))
                    .clickable { onAddClick() },
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
                        imageVector = Icons.Rounded.AddCircleOutline,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (memories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Brak zdjęć w galerii", color = Color.Gray)
                }
            } else {
                // Fixed: Regular Grid with uniform aspect ratio cards
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(memories) { trip ->
                        GalleryCard(
                            trip = trip,
                            onClick = { onTripClick(trip.tripId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryCard(trip: TripEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f) 
            .shadow(6.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (trip.mainImageUri.isNotEmpty()) {
                AsyncImage(
                    model = trip.mainImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Crop to fill the uniform area
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Structured Placeholder matching Figma
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ImageNotSupported,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                }
            }
            
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = trip.title,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(trip.dateTimestamp))
                    
                    if (trip.location.isNotBlank()) {
                        Icon(
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = TripKeepOrange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        
                        Text(
                            text = "${trip.location} • $formattedDate",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Text(
                            text = formattedDate,
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryTopBar(onBackClick: () -> Unit) {
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
        Text("Galeria", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
        Box(modifier = Modifier.size(40.dp))
    }
}
