package pl.yskp.tripkeep.data.repository

import kotlinx.coroutines.flow.Flow
import pl.yskp.tripkeep.data.local.dao.TripDao
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity

interface TripRepository {
    fun getAllMemoriesStream(): Flow<List<TripEntity>>
    fun getAllPlansStream(): Flow<List<TripEntity>>
    fun getTripStream(id: Long): Flow<TripEntity?>
    fun getImagesForTripStream(tripId: Long): Flow<List<TripImageEntity>>
    fun getImageCountStream(): Flow<Int>
    suspend fun insertTrip(trip: TripEntity): Long
    suspend fun updateTrip(trip: TripEntity)
    suspend fun insertImage(image: TripImageEntity): Long
    suspend fun deleteImagesForTrip(tripId: Long)
    suspend fun updateTripStatus(tripId: Long, isPlanned: Boolean)
    suspend fun deleteTrip(trip: TripEntity)
    suspend fun deleteAll()
}

class OfflineTripRepository(private val tripDao: TripDao) : TripRepository {
    override fun getAllMemoriesStream(): Flow<List<TripEntity>> = tripDao.getAllMemories()
    override fun getAllPlansStream(): Flow<List<TripEntity>> = tripDao.getAllPlans()
    override fun getTripStream(id: Long): Flow<TripEntity?> = tripDao.getTripById(id)
    override fun getImagesForTripStream(tripId: Long): Flow<List<TripImageEntity>> = tripDao.getImagesForTrip(tripId)
    override fun getImageCountStream(): Flow<Int> = tripDao.getImageCount()
    override suspend fun insertTrip(trip: TripEntity): Long = tripDao.insertTrip(trip)
    override suspend fun updateTrip(trip: TripEntity) = tripDao.updateTrip(trip)
    override suspend fun insertImage(image: TripImageEntity): Long = tripDao.insertImage(image)
    override suspend fun deleteImagesForTrip(tripId: Long) = tripDao.deleteImagesForTrip(tripId)
    override suspend fun updateTripStatus(tripId: Long, isPlanned: Boolean) = tripDao.updateTripStatus(tripId, isPlanned)
    override suspend fun deleteTrip(trip: TripEntity) = tripDao.deleteTrip(trip)
    override suspend fun deleteAll() = tripDao.deleteAllTrips()
}
