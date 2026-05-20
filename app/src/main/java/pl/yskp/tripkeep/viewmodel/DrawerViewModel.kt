package pl.yskp.tripkeep.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import pl.yskp.tripkeep.data.UserPreferences
import pl.yskp.tripkeep.data.UserPreferencesRepository

class DrawerViewModel(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UserPreferences()
        )
}
