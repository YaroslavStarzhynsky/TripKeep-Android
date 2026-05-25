package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity
import pl.yskp.tripkeep.data.repository.TripRepository

data class TripDetailsUiState(
    val trip: TripEntity? = null,
    val additionalImages: List<TripImageEntity> = emptyList()
)

class TripDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val tripId: Long = checkNotNull(savedStateHandle["tripId"])

    val uiState: StateFlow<TripDetailsUiState> = combine(
        tripRepository.getTripStream(tripId),
        tripRepository.getImagesForTripStream(tripId)
    ) { trip, images ->
        TripDetailsUiState(trip, images)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TripDetailsUiState()
    )
}
