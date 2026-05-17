package pl.yskp.tripkeep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.first
import pl.yskp.tripkeep.ui.navigation.TripKeepNavHost
import pl.yskp.tripkeep.ui.navigation.TripKeepScreen
import pl.yskp.tripkeep.ui.theme.TripKeepTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userPreferencesRepository = (application as TripKeepApplication).container.userPreferencesRepository

        setContent {
            TripKeepTheme {
                var startDestination by remember { mutableStateOf<String?>(null) }
                val navController = rememberNavController()

                LaunchedEffect(Unit) {
                    val prefs = userPreferencesRepository.userPreferencesFlow.first()
                    startDestination = if (prefs.userName.isNotEmpty()) {
                        TripKeepScreen.Home.name
                    } else {
                        TripKeepScreen.Welcome.name
                    }
                }

                if (startDestination != null) {
                    TripKeepNavHost(
                        navController = navController,
                        startDestination = startDestination!!
                    )
                }
            }
        }
    }
}
