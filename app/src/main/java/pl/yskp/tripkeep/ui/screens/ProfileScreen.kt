package pl.yskp.tripkeep.ui.screens

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.navigation.TripKeepScreen
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onPlansClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { 
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewModel.updateAvatar(it.toString()) 
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Resetuj wszystko") },
            text = { Text("Czy na pewno chcesz usunąć wszystkie dane? Tej operacji nie da się cofnąć.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllData()
                        onLogout()
                        showResetDialog = false
                    }
                ) {
                    Text("Tak, usuń wszystko", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Anuluj")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            ProfileTopBar(onBackClick = onHomeClick)
        },
        bottomBar = {
            HomeBottomBar(
                currentScreen = TripKeepScreen.Profile,
                onHomeClick = onHomeClick,
                onGalleryClick = onGalleryClick,
                onPlansClick = onPlansClick,
                onProfileClick = {}
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Profile Photo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            ) {
                if (uiState.userPreferences.userAvatarUri.isNotEmpty()) {
                    AsyncImage(
                        model = uiState.userPreferences.userAvatarUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp).align(Alignment.Center),
                        tint = Color.White
                    )
                }
            }

            TextButton(
                onClick = { imagePickerLauncher.launch("image/*") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "Edytuj zdjęcie profilowe",
                    color = TripKeepOrange,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Name and Location
            Text(
                text = uiState.userPreferences.userName.ifEmpty { "Użytkownik" },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.userPreferences.userCity.isNotBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = TripKeepOrange
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = uiState.userPreferences.userCity,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Stats Grid
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(label = "Wyprawy", value = uiState.tripCount.toString(), modifier = Modifier.weight(1f))
                    StatCard(label = "Zdjęcia", value = uiState.photoCount.toString(), modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(label = "Kraje", value = uiState.countryCount.toString(), modifier = Modifier.weight(1f))
                    StatCard(label = "Plany", value = uiState.planCount.toString(), modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Reset Button
            TextButton(
                onClick = {
                    showResetDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.Logout,
                        contentDescription = null,
                        tint = Color.Red
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Resetuj wszystko",
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(85.dp).shadow(6.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TripKeepOrange
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileTopBar(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
        }
        
        Text(
            text = "Konto",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )

        Box(modifier = Modifier.size(40.dp))
    }
}
