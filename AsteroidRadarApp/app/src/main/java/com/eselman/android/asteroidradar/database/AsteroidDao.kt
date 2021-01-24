package com.eselman.android.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from asteroidentity WHERE closeApproachDate = :currentDate")
    fun getTodayAsteroids(currentDate: String): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroidentity WHERE closeApproachDate >= :dateStart AND closeApproachDate <= :dateEnd ORDER BY closeApproachDate")
    fun getWeekAsteroids(dateStart: String, dateEnd: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroidentity ORDER BY closeApproachDate")
    fun getAllAsteroids(): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidEntity)

    @Query("DELETE FROM asteroidentity")
    fun deleteAsteroids()
}