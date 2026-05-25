package pl.yskp.tripkeep.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.TripDetailsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TripDetailsScreen(
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: TripDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color(0xFFF5F5F5) 
    ) { innerPadding ->
        uiState.trip?.let { data ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 1. Hero Image
                AsyncImage(
                    model = data.mainImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.45f),
                    contentScale = ContentScale.Crop
                )

                // 2. Content Panel
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 280.dp) 
                        .padding(horizontal = 16.dp) 
                        .shadow(elevation = 12.dp, shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(Color.White)
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp)
                ) {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    if (data.location.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = TripKeepOrange
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = data.location,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(data.dateTimestamp))
                            Text(text = formattedDate, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    } else {
                        val formattedDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(data.dateTimestamp))
                        Text(text = formattedDate, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = data.description.ifEmpty { "Brak opisu tej podróży." },
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 24.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Memories Section
                    Text("Wspomnienia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (uiState.additionalImages.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Brak dodatkowych zdjęć", color = Color.Gray)
                        }
                    } else {
                        LazyHorizontalGrid(
                            rows = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            items(uiState.additionalImages) { image ->
                                AsyncImage(
                                    model = image.imageUri,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
                                        .clip(RoundedCornerShape(16.dp))
                                        .clickable { selectedImageUri = image.imageUri },
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Video Section
                    Text("Filmowe wspomnienie", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    if (!data.videoUri.isNullOrEmpty()) {
                        VideoPlayer(videoUri = Uri.parse(data.videoUri))
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFF5F5F5)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Brak wideo", color = Color.Gray)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(100.dp))
                }

                // Floating Buttons
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    IconButton(
                        onClick = { onEditClick(data.tripId) },
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = Color.White)
                    }
                }
            }
        }

        // Image Preview Dialog
        if (selectedImageUri != null) {
            ImagePreviewDialog(
                imageUri = selectedImageUri!!,
                onDismiss = { selectedImageUri = null }
            )
        }
    }
}

@Composable
fun ImagePreviewDialog(imageUri: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.9f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + scaleIn(initialScale = 0.8f, animationSpec = tween(300)),
                exit = fadeOut(tween(300)) + scaleOut(targetScale = 0.8f, animationSpec = tween(300))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.8f)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )
                    
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Rounded.Close, contentDescription = "Close", tint = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                clipToOutline = true
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
    )
}
