package pl.yskp.tripkeep.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity

@Dao
interface TripDao {

    @Query("SELECT * FROM trips WHERE isPlanned = 0 ORDER BY dateTimestamp DESC")
    fun getAllMemories(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE isPlanned = 1 ORDER BY dateTimestamp ASC")
    fun getAllPlans(): Flow<List<TripEntity>>

    @Query("SELECT * FROM trips WHERE tripId = :tripId")
    fun getTripById(tripId: Long): Flow<TripEntity?>

    @Query("SELECT * FROM trip_images WHERE ownerTripId = :tripId")
    fun getImagesForTrip(tripId: Long): Flow<List<TripImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(image: TripImageEntity): Long

    @Update
    suspend fun updateTrip(trip: TripEntity)

    @Query("UPDATE trips SET isPlanned = :isPlanned WHERE tripId = :tripId")
    suspend fun updateTripStatus(tripId: Long, isPlanned: Boolean)

    @Query("SELECT COUNT(*) FROM trip_images")
    fun getImageCount(): Flow<Int>

    @Query("DELETE FROM trips")
    suspend fun deleteAllTrips()

    @Delete
    suspend fun deleteTrip(trip: TripEntity)
}
