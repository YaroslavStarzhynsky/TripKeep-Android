package pl.yskp.tripkeep.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import pl.yskp.tripkeep.ui.AppViewModelProvider
import pl.yskp.tripkeep.ui.screens.HomeScreen
import pl.yskp.tripkeep.ui.screens.PlannerScreen
import pl.yskp.tripkeep.ui.screens.ProfileScreen
import pl.yskp.tripkeep.ui.screens.SetupScreen
import pl.yskp.tripkeep.ui.screens.WelcomeScreen
import pl.yskp.tripkeep.ui.theme.TripKeepOrange
import pl.yskp.tripkeep.viewmodel.DrawerViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

enum class TripKeepScreen {
    Welcome,
    Setup,
    Home,
    Gallery,
    Planner,
    Profile,
    Details
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute != TripKeepScreen.Welcome.name && currentRoute != TripKeepScreen.Setup.name,
        drawerContent = {
            TripKeepDrawerContent(
                selectedRoute = currentRoute,
                onItemClick = { screen ->
                    scope.launch { drawerState.close() }
                    if (screen.name != currentRoute) {
                        navController.navigate(screen.name) {
                            launchSingleTop = true
                        }
                    }
                },
                onLogoutClick = {
                    scope.launch { 
                        drawerState.close()
                        navController.navigate(TripKeepScreen.Welcome.name) {
                            popUpTo(0) { inclusive = true }
                        }
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
                        navController.navigate(TripKeepScreen.Profile.name)
                    },
                    onGalleryClick = {
                        navController.navigate(TripKeepScreen.Gallery.name)
                    },
                    onPlansClick = {
                        navController.navigate(TripKeepScreen.Planner.name)
                    }
                )
            }
            composable(route = TripKeepScreen.Profile.name) {
                ProfileScreen(
                    onHomeClick = {
                        navController.navigate(TripKeepScreen.Home.name) {
                            popUpTo(TripKeepScreen.Home.name) { inclusive = true }
                        }
                    },
                    onGalleryClick = {
                        navController.navigate(TripKeepScreen.Gallery.name)
                    },
                    onPlansClick = {
                        navController.navigate(TripKeepScreen.Planner.name)
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
                        navController.navigate(TripKeepScreen.Home.name) {
                            popUpTo(TripKeepScreen.Home.name) { inclusive = true }
                        }
                    },
                    onAddTripClick = {
                        // navController.navigate(TripKeepScreen.AddTrip.name)
                    },
                    onHomeClick = {
                        navController.navigate(TripKeepScreen.Home.name)
                    },
                    onGalleryClick = {
                        navController.navigate(TripKeepScreen.Gallery.name)
                    },
                    onProfileClick = {
                        navController.navigate(TripKeepScreen.Profile.name)
                    },
                    onRealizedClick = { tripId ->
                        // navController.navigate("${TripKeepScreen.Details.name}/$tripId/edit")
                    }
                )
            }
            // Placeholders for other screens
            composable(route = TripKeepScreen.Gallery.name) { }
            composable(route = TripKeepScreen.Details.name) { }
        }
    }
}

@Composable
fun TripKeepDrawerContent(
    selectedRoute: String?,
    viewModel: DrawerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onItemClick: (TripKeepScreen) -> Unit,
    onLogoutClick: () -> Unit
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
            // Profile Header
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
                            Icons.Default.Person,
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

            // Navigation List
            val items = listOf(
                Triple("Home", Icons.Default.Home, TripKeepScreen.Home),
                Triple("Galeria", Icons.Default.Image, TripKeepScreen.Gallery),
                Triple("Plany", Icons.Default.CalendarMonth, TripKeepScreen.Planner),
                Triple("Konto", Icons.Default.Person, TripKeepScreen.Profile)
            )

            items.forEach { (label, icon, screen) ->
                NavigationDrawerItem(
                    label = { Text(label) },
                    selected = selectedRoute == screen.name,
                    onClick = { onItemClick(screen) },
                    icon = { Icon(icon, contentDescription = null) },
                    shape = RoundedCornerShape(16.dp),
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

            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            TextButton(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Wyloguj", color = Color.Red)
                }
            }
        }
    }
}
