package pl.yskp.tripkeep.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import pl.yskp.tripkeep.TripKeepApplication
import pl.yskp.tripkeep.viewmodel.AddTripViewModel
import pl.yskp.tripkeep.viewmodel.DrawerViewModel
import pl.yskp.tripkeep.viewmodel.GalleryViewModel
import pl.yskp.tripkeep.viewmodel.HomeViewModel
import pl.yskp.tripkeep.viewmodel.PlannerViewModel
import pl.yskp.tripkeep.viewmodel.ProfileViewModel
import pl.yskp.tripkeep.viewmodel.SetupViewModel
import pl.yskp.tripkeep.viewmodel.TripDetailsViewModel
import androidx.lifecycle.createSavedStateHandle

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
        initializer {
            DrawerViewModel(
                tripKeepApplication().container.userPreferencesRepository
            )
        }
        initializer {
            ProfileViewModel(
                tripKeepApplication().container.tripRepository,
                tripKeepApplication().container.userPreferencesRepository
            )
        }
        initializer {
            PlannerViewModel(
                tripKeepApplication().container.tripRepository,
                tripKeepApplication().container.userPreferencesRepository
            )
        }
        initializer {
            AddTripViewModel(
                this.createSavedStateHandle(),
                tripKeepApplication().container.tripRepository
            )
        }
        initializer {
            GalleryViewModel(
                tripKeepApplication().container.tripRepository
            )
        }
        initializer {
            TripDetailsViewModel(
                this.createSavedStateHandle(),
                tripKeepApplication().container.tripRepository
            )
        }
    }
}

fun CreationExtras.tripKeepApplication(): TripKeepApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TripKeepApplication)
