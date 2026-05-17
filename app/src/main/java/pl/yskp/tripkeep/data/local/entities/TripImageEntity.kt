package pl.yskp.tripkeep.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "trip_images",
    foreignKeys = [
        ForeignKey(
            entity = TripEntity::class,
            parentColumns = ["tripId"],
            childColumns = ["ownerTripId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["ownerTripId"])]
)
data class TripImageEntity(
    @PrimaryKey(autoGenerate = true)
    val imageId: Long = 0,
    val ownerTripId: Long,
    val imageUri: String
)
