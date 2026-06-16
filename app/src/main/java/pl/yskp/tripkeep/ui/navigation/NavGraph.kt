package pl.yskp.tripkeep.ui.navigation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import pl.yskp.tripkeep.R
import androidx.navigation.NavGraph.Companion.findStartDestination
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.screens.*
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.DrawerViewModel

enum class TripKeepScreen {
    Welcome,
    Setup,
    Home,
    Gallery,
    Planner,
    Profile,
    Details,
    AddTrip,
    About
}

@Composable
fun TripKeepNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showAboutDialog by remember { mutableStateOf(false) }

    // Navigation helper for tabs
    val navigateToTab = { screen: TripKeepScreen ->
        navController.navigate(screen.name) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Close drawer on back press if it's open
    if (drawerState.isOpen) {
        BackHandler {
            scope.launch { drawerState.close() }
        }
    }

    if (showAboutDialog) {
        AboutAppDialog(onDismiss = { showAboutDialog = false })
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != TripKeepScreen.Welcome.name && currentRoute != TripKeepScreen.Setup.name,
        drawerContent = {
            TripKeepDrawerContent(
                selectedRoute = currentRoute,
                onItemClick = { screen ->
                    scope.launch { drawerState.close() }
                    if (screen == TripKeepScreen.About) {
                        showAboutDialog = true
                    } else if (screen.name != currentRoute) {
                        navigateToTab(screen)
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
        ) {
            composable(route = TripKeepScreen.Welcome.name) {
                WelcomeScreen(
                    onContinueClick = {
                        navController.navigate(TripKeepScreen.Setup.name)
                    }
                )
            }
            composable(route = TripKeepScreen.Setup.name) {
                SetupScreen(
                    onProfileCreated = {
                        navController.navigate(TripKeepScreen.Home.name) {
                            popUpTo(TripKeepScreen.Welcome.name) { inclusive = true }
                        }
                    }
                )
            }
            composable(route = TripKeepScreen.Home.name) {
                HomeScreen(
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    onProfileClick = {
                        navigateToTab(TripKeepScreen.Profile)
                    },
                    onGalleryClick = {
                        navigateToTab(TripKeepScreen.Gallery)
                    },
                    onPlansClick = {
                        navigateToTab(TripKeepScreen.Planner)
                    },
                    onTripClick = { tripId ->
                        navController.navigate("${TripKeepScreen.Details.name}/$tripId")
                    }
                )
            }
            composable(route = TripKeepScreen.Profile.name) {
                ProfileScreen(
                    onHomeClick = {
                        navigateToTab(TripKeepScreen.Home)
                    },
                    onGalleryClick = {
                        navigateToTab(TripKeepScreen.Gallery)
                    },
                    onPlansClick = {
                        navigateToTab(TripKeepScreen.Planner)
                    },
                    onLogout = {
                        navController.navigate(TripKeepScreen.Welcome.name) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(route = TripKeepScreen.Planner.name) {
                PlannerScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAddTripClick = {
                        navController.navigate(TripKeepScreen.AddTrip.name)
                    },
                    onHomeClick = {
                        navigateToTab(TripKeepScreen.Home)
                    },
                    onGalleryClick = {
                        navigateToTab(TripKeepScreen.Gallery)
                    },
                    onProfileClick = {
                        navigateToTab(TripKeepScreen.Profile)
                    },
                    onRealizedClick = { tripId ->
                        navController.navigate("${TripKeepScreen.AddTrip.name}?tripId=$tripId")
                    },
                    onTripClick = { tripId ->
                        navController.navigate("${TripKeepScreen.Details.name}/$tripId")
                    }
                )
            }
            composable(
                route = "${TripKeepScreen.AddTrip.name}?tripId={tripId}",
                arguments = listOf(
                    navArgument("tripId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
            ) {
                AddTripScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable(route = TripKeepScreen.Gallery.name) {
                GalleryScreen(
                    onBackClick = { navController.popBackStack() },
                    onHomeClick = { navigateToTab(TripKeepScreen.Home) },
                    onPlansClick = { navigateToTab(TripKeepScreen.Planner) },
                    onProfileClick = { navigateToTab(TripKeepScreen.Profile) },
                    onTripClick = { tripId ->
                        navController.navigate("${TripKeepScreen.Details.name}/$tripId")
                    },
                    onAddClick = {
                        navController.navigate(TripKeepScreen.AddTrip.name)
                    }
                )
            }
            composable(
                route = "${TripKeepScreen.Details.name}/{tripId}",
                arguments = listOf(navArgument("tripId") { type = NavType.LongType })
            ) {
                TripDetailsScreen(
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { tripId ->
                        navController.navigate("${TripKeepScreen.AddTrip.name}?tripId=$tripId")
                    }
                )
            }
        }
    }
}

@Composable
fun TripKeepDrawerContent(
    selectedRoute: String?,
    viewModel: DrawerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onItemClick: (TripKeepScreen) -> Unit
) {
    val userPrefs by viewModel.userPreferences.collectAsState()

    ModalDrawerSheet(
        drawerContainerColor = Color.White,
        drawerShape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.fillMaxHeight().width(300.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    if (userPrefs.userAvatarUri.isNotEmpty()) {
                        AsyncImage(
                            model = userPrefs.userAvatarUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center),
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = userPrefs.userName.ifEmpty { "Użytkownik" },
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userPrefs.userCity.ifEmpty { "Twoje miasto" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val items = listOf(
                Triple("Home", Icons.Rounded.Home, TripKeepScreen.Home),
                Triple("Galeria", Icons.Rounded.Image, TripKeepScreen.Gallery),
                Triple("Plany", Icons.Rounded.CalendarMonth, TripKeepScreen.Planner),
                Triple("Konto", Icons.Rounded.Person, TripKeepScreen.Profile),
                Triple("O aplikacji", Icons.Rounded.Info, TripKeepScreen.About)
            )

            items.forEach { (label, icon, screen) ->
                NavigationDrawerItem(
                    label = { 
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = if (selectedRoute == screen.name) FontWeight.Bold else FontWeight.Medium
                        ) 
                    },
                    selected = selectedRoute == screen.name,
                    onClick = { onItemClick(screen) },
                    icon = { Icon(icon, contentDescription = null) },
                    shape = RoundedCornerShape(24.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = TripKeepOrange.copy(alpha = 0.1f),
                        unselectedContainerColor = Color.Transparent,
                        selectedTextColor = TripKeepOrange,
                        unselectedTextColor = Color.Black,
                        selectedIconColor = TripKeepOrange,
                        unselectedIconColor = Color.Gray
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun AboutAppDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val resId = context.resources.getIdentifier("app_audio", "raw", context.packageName)
            if (resId != 0) {
                val audioUri = Uri.parse("android.resource://${context.packageName}/$resId")
                setMediaItem(MediaItem.fromUri(audioUri))
                prepare()
            }
        }
    }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    AlertDialog(
        onDismissRequest = {
            exoPlayer.pause()
            onDismiss()
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "TripKeep v1.0",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Twórcy: Yaroslav Starzhynskyi & Krzysztof Pawlaczek\n\n" +
                            "Gratulujemy odnalezienia tej sekcji! Stworzyliśmy TripKeep, aby pomóc Ci zachować najważniejsze momenty z Twoich wypraw. W nagrodę przygotowaliśmy dla Ciebie dźwiękowy klimat podróży.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Start
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                IconButton(
                    onClick = {
                        if (isPlaying) {
                            exoPlayer.pause()
                        } else {
                            exoPlayer.play()
                        }
                        isPlaying = !isPlaying
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(TripKeepOrange.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = TripKeepOrange,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Kod źródłowy:",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "https://github.com/YaroslavStarzhynsky/TripKeep-Android",
                    style = MaterialTheme.typography.bodySmall,
                    color = TripKeepOrange,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                exoPlayer.pause()
                onDismiss()
            }) {
                Text("Zamknij", color = TripKeepOrange, fontWeight = FontWeight.Bold)
            }
        }
    )
}
