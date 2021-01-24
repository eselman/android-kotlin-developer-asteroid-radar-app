package com.eselman.android.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eselman.android.asteroidradar.common.extensions.toDateFormat
import com.eselman.android.asteroidradar.common.model.Asteroid

@Entity
data class AsteroidEntity constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid (
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate.toDateFormat(),
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
            )
    }
}