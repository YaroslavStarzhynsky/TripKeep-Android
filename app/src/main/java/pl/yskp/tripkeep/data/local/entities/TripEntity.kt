package pl.yskp.tripkeep.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val tripId: Long = 0,
    val title: String,
    val location: String,
    val dateTimestamp: Long,
    val description: String,
    val mainImageUri: String,
    val videoUri: String?,
    val voiceNoteUri: String?,
    val isPlanned: Boolean
)
