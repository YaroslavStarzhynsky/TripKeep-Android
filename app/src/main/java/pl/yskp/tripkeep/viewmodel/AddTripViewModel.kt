package pl.yskp.tripkeep.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity
import pl.yskp.tripkeep.data.repository.TripRepository

class AddTripViewModel(
    savedStateHandle: SavedStateHandle,
    private val tripRepository: TripRepository
) : ViewModel() {

    private val tripId: Long? = savedStateHandle["tripId"]

    var title by mutableStateOf("")
        private set

    var location by mutableStateOf("")
        private set

    var dateTimestamp by mutableStateOf(System.currentTimeMillis())
        private set

    var description by mutableStateOf("")
        private set

    var mainImageUri by mutableStateOf("")
        private set
        
    var additionalImageUris by mutableStateOf<List<String>>(emptyList())
        private set

    var videoUri by mutableStateOf<String?>(null)
        private set

    var isPlanned by mutableStateOf(true)
        private set

    init {
        viewModelScope.launch {
            if (tripId != null && tripId != -1L) {
                val trip = tripRepository.getTripStream(tripId).filterNotNull().first()
                title = trip.title
                location = trip.location
                dateTimestamp = trip.dateTimestamp
                description = trip.description
                mainImageUri = trip.mainImageUri
                videoUri = trip.videoUri
                isPlanned = trip.isPlanned
                
                val images = tripRepository.getImagesForTripStream(tripId).first()
                additionalImageUris = images.map { it.imageUri }
            }
        }
    }

    fun updateTitle(value: String) { title = value }
    fun updateLocation(value: String) { location = value }
    fun updateDate(value: Long) { dateTimestamp = value }
    fun updateDescription(value: String) { description = value }
    fun updateImageUri(value: String) { mainImageUri = value }
    fun updateAdditionalImages(uris: List<String>) { additionalImageUris = uris }
    fun updateVideoUri(uri: String?) { videoUri = uri }
    fun updateIsPlanned(value: Boolean) { isPlanned = value }

    suspend fun saveTrip() {
        if (title.isBlank()) return
        
        val currentTripId = if (tripId == null || tripId == -1L) 0L else tripId
        
        val trip = TripEntity(
            tripId = currentTripId,
            title = title,
            location = location,
            dateTimestamp = dateTimestamp,
            description = description,
            mainImageUri = mainImageUri,
            videoUri = videoUri,
            voiceNoteUri = null,
            isPlanned = isPlanned
        )
        
        val savedId = if (currentTripId == 0L) {
            tripRepository.insertTrip(trip)
        } else {
            // Fix: Clear existing images before updating to prevent duplication
            tripRepository.deleteImagesForTrip(currentTripId)
            tripRepository.updateTrip(trip)
            currentTripId
        }
        
        additionalImageUris.forEach { uri ->
            tripRepository.insertImage(
                TripImageEntity(
                    ownerTripId = savedId,
                    imageUri = uri
                )
            )
        }
    }
}
