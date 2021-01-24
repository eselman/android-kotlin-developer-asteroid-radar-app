package com.eselman.android.asteroidradar.common.model

import com.eselman.android.asteroidradar.common.extensions.toStringFormat
import com.eselman.android.asteroidradar.database.AsteroidEntity

object AsteroidMapper {
    fun asDatabaseModel(asteroids: List<Asteroid>): Array<AsteroidEntity> {
        return asteroids.map {
            AsteroidEntity(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate.toStringFormat(),
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
            )
        }.toTypedArray()
    }
}