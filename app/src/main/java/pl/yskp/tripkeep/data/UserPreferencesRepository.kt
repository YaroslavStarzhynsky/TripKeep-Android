package pl.yskp.tripkeep.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class UserPreferences(
    val userName: String = "",
    val userCity: String = "",
    val userAvatarUri: String = ""
)

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_CITY = stringPreferencesKey("user_city")
        val USER_AVATAR_URI = stringPreferencesKey("user_avatar_uri")
        const val TAG = "UserPreferencesRepo"
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserPreferences(
                userName = preferences[USER_NAME] ?: "",
                userCity = preferences[USER_CITY] ?: "",
                userAvatarUri = preferences[USER_AVATAR_URI] ?: ""
            )
        }

    suspend fun saveUserPreferences(userName: String, userCity: String, userAvatarUri: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
            preferences[USER_CITY] = userCity
            preferences[USER_AVATAR_URI] = userAvatarUri
        }
    }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
