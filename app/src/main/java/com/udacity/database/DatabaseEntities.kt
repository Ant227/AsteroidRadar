package com.udacity.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "table_asteroid")
data class DatabaseAsteroid constructor(
    @PrimaryKey val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename =  it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

@Entity(tableName = "table_picOfDay")
data class DatabasePicOfDay constructor(
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String

)

fun DatabasePicOfDay.asDomainModel(): PictureOfDay {
    return let {
        com.udacity.asteroidradar.PictureOfDay(
            url = it.url,
            mediaType = it.mediaType,
            title = it.title
        )
    }
}