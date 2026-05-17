package pl.yskp.tripkeep.data.repository

import kotlinx.coroutines.flow.Flow
import pl.yskp.tripkeep.data.local.dao.TripDao
import pl.yskp.tripkeep.data.local.entities.TripEntity
import pl.yskp.tripkeep.data.local.entities.TripImageEntity

interface TripRepository {
    fun getAllMemoriesStream(): Flow<List<TripEntity>>
    fun getAllPlansStream(): Flow<List<TripEntity>>
    fun getTripStream(id: Long): Flow<TripEntity?>
    suspend fun insertTrip(trip: TripEntity): Long
    suspend fun insertImage(image: TripImageEntity): Long
    suspend fun deleteTrip(trip: TripEntity)
}

class OfflineTripRepository(private val tripDao: TripDao) : TripRepository {
    override fun getAllMemoriesStream(): Flow<List<TripEntity>> = tripDao.getAllMemories()
    override fun getAllPlansStream(): Flow<List<TripEntity>> = tripDao.getAllPlans()
    override fun getTripStream(id: Long): Flow<TripEntity?> = tripDao.getTripById(id)
    override suspend fun insertTrip(trip: TripEntity): Long = tripDao.insertTrip(trip)
    override suspend fun insertImage(image: TripImageEntity): Long = tripDao.insertImage(image)
    override suspend fun deleteTrip(trip: TripEntity) = tripDao.deleteTrip(trip)
}
