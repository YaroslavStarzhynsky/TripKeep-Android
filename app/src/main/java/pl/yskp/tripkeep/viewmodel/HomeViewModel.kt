package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.yskp.tripkeep.data.UserPreferencesRepository
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.repository.TripRepository

data class HomeUiState(
    val userName: String = "",
    val avatarUri: String = "",
    val memories: List<TripEntity> = emptyList(),
    val plans: List<TripEntity> = emptyList()
)

class HomeViewModel(
    private val tripRepository: TripRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = userPreferencesRepository.userPreferencesFlow
        .map { preferences ->
            HomeUiState(
                userName = preferences.userName,
                avatarUri = preferences.userAvatarUri
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = HomeUiState()
        )

    val memories: StateFlow<List<TripEntity>> = tripRepository.getAllMemoriesStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    val plans: StateFlow<List<TripEntity>> = tripRepository.getAllPlansStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}
