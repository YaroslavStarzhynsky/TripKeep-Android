package pl.yskp.tripkeep.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import pl.yskp.tripkeep.data.UserPreferencesRepository

class SetupViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    var userName by mutableStateOf("")
        private set

    var userCity by mutableStateOf("")
        private set

    var userAvatarUri by mutableStateOf("")
        private set

    fun updateName(name: String) {
        userName = name
    }

    fun updateCity(city: String) {
        userCity = city
    }

    fun updateAvatar(uri: String) {
        userAvatarUri = uri
    }

    suspend fun saveProfile() {
        userPreferencesRepository.saveUserPreferences(userName, userCity, userAvatarUri)
    }
}
