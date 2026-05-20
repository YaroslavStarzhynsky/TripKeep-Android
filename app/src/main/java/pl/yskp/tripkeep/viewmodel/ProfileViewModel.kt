package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.yskp.tripkeep.data.UserPreferences
import pl.yskp.tripkeep.data.UserPreferencesRepository
import pl.yskp.tripkeep.data.repository.TripRepository

data class ProfileUiState(
    val userPreferences: UserPreferences = UserPreferences(),
    val tripCount: Int = 0,
    val photoCount: Int = 0,
    val countryCount: Int = 0,
    val planCount: Int = 0
)

class ProfileViewModel(
    private val tripRepository: TripRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        userPreferencesRepository.userPreferencesFlow,
        tripRepository.getAllMemoriesStream(),
        tripRepository.getAllPlansStream(),
        tripRepository.getImageCountStream()
    ) { prefs, memories, plans, imageCount ->
        ProfileUiState(
            userPreferences = prefs,
            tripCount = memories.size,
            photoCount = imageCount,
            countryCount = memories.map { it.location.split(",").last().trim() }.distinct().size,
            planCount = plans.size
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProfileUiState()
    )

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearUserPreferences()
        }
    }

    fun updateAvatar(uri: String) {
        viewModelScope.launch {
            val current = uiState.value.userPreferences
            userPreferencesRepository.saveUserPreferences(
                current.userName,
                current.userCity,
                uri
            )
        }
    }
}
