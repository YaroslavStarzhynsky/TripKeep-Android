package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.repository.TripRepository

class GalleryViewModel(
    private val tripRepository: TripRepository
) : ViewModel() {
    val memories: StateFlow<List<TripEntity>> = tripRepository.getAllMemoriesStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
