package pl.yskp.tripkeep.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.yskp.tripkeep.ui.screens.HomeScreen
import pl.yskp.tripkeep.ui.screens.SetupScreen
import pl.yskp.tripkeep.ui.screens.WelcomeScreen

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
            HomeScreen()
        }
        // Placeholders for other screens
        composable(route = TripKeepScreen.Gallery.name) { }
        composable(route = TripKeepScreen.Planner.name) { }
        composable(route = TripKeepScreen.Profile.name) { }
        composable(route = TripKeepScreen.Details.name) { }
    }
}
