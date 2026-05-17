package pl.yskp.tripkeep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.yskp.tripkeep.data.local.dao.TripDao
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity

@Database(
    entities = [TripEntity::class, TripImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
}
