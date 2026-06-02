package pl.yskp.tripkeep.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.SetupViewModel

@Composable
fun SetupScreen(
    onProfileCreated: () -> Unit,
    viewModel: SetupViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { 
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            viewModel.updateAvatar(it.toString()) 
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(32.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Cześć!",
                    style = MaterialTheme.typography.displayMedium.copy(
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.Default.Terrain,
                    contentDescription = null,
                    tint = TripKeepOrange,
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Text(
                text = "Przygotujmy Twój profil",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (viewModel.userAvatarUri.isNotEmpty()) {
                            AsyncImage(
                                model = viewModel.userAvatarUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Color.Gray
                            )
                        }
                    }

                    TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text(
                            text = "Dodaj zdjęcie profilowe",
                            color = TripKeepOrange,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    SetupTextField(
                        value = viewModel.userName,
                        onValueChange = { viewModel.updateName(it) },
                        label = "Jak mamy się do Ciebie zwracać?",
                        placeholder = "Imię"
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    SetupTextField(
                        value = viewModel.userCity,
                        onValueChange = { viewModel.updateCity(it) },
                        label = "Skąd zaczynasz swoje podróże?",
                        placeholder = "Miasto, Kraj"
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveProfile()
                        onProfileCreated()
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Gray
                )
            ) {
                Text(
                    text = "Kontynuuj",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(TripKeepOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            
            // Extra spacer to ensure scrolling comfort
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SetupTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TripKeepOrange,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )
    }
}
