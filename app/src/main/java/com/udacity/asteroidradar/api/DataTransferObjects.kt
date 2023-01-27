package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.database.DatabaseAsteroid
import com.udacity.database.DatabasePicOfDay
import java.util.ArrayList

data class NetworkPictureOfDay(
    val title: String,

    @Json(name = "media_type")
    val mediaType: String,

    val url: String
)
fun List<Asteroid>.asDatabaseModel(): List<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
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

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePicOfDay {
    return let {
        DatabasePicOfDay(
            url = it.url,
            mediaType = it.mediaType,
            title = it.title
        )
    }
}