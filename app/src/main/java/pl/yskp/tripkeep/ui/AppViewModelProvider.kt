package pl.yskp.tripkeep.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import pl.yskp.tripkeep.TripKeepApplication
import pl.yskp.tripkeep.viewmodel.HomeViewModel
import pl.yskp.tripkeep.viewmodel.SetupViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(
                tripKeepApplication().container.tripRepository,
                tripKeepApplication().container.userPreferencesRepository
            )
        }
        initializer {
            SetupViewModel(
                tripKeepApplication().container.userPreferencesRepository
            )
        }
    }
}

fun CreationExtras.tripKeepApplication(): TripKeepApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TripKeepApplication)
