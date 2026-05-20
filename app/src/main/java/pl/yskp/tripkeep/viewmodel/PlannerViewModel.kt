package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.yskp.tripkeep.data.UserPreferencesRepository
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.repository.TripRepository

data class PlannerUiState(
    val plannedTrips: List<TripEntity> = emptyList(),
    val avatarUri: String = ""
)

class PlannerViewModel(
    private val tripRepository: TripRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val uiState: StateFlow<PlannerUiState> = combine(
        tripRepository.getAllPlansStream(),
        userPreferencesRepository.userPreferencesFlow
    ) { plans, prefs ->
        PlannerUiState(
            plannedTrips = plans,
            avatarUri = prefs.userAvatarUri
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = PlannerUiState()
    )

    fun markAsRealized(tripId: Long) {
        viewModelScope.launch {
            tripRepository.updateTripStatus(tripId, false)
        }
    }
}
