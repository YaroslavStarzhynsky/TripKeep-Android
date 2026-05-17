package pl.yskp.tripkeep

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import pl.yskp.tripkeep.data.AppContainer
import pl.yskp.tripkeep.data.AppDataContainer

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class TripKeepApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this, dataStore)
    }
}
