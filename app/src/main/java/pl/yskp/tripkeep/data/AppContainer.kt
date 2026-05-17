package pl.yskp.tripkeep.data

import android.content.Context
import androidx.room.Room
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import pl.yskp.tripkeep.data.local.AppDatabase
import pl.yskp.tripkeep.data.repository.OfflineTripRepository
import pl.yskp.tripkeep.data.repository.TripRepository

interface AppContainer {
    val tripRepository: TripRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class AppDataContainer(
    private val context: Context,
    private val dataStore: DataStore<Preferences>
) : AppContainer {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "trip_keep_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    override val tripRepository: TripRepository by lazy {
        OfflineTripRepository(database.tripDao())
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(dataStore)
    }
}
