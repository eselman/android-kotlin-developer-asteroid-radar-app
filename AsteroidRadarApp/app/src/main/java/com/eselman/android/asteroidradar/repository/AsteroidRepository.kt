package com.eselman.android.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.eselman.android.asteroidradar.BuildConfig
import com.eselman.android.asteroidradar.api.NetworkUtils
import com.eselman.android.asteroidradar.api.RetrofitHelper
import com.eselman.android.asteroidradar.common.extensions.toStringFormat
import com.eselman.android.asteroidradar.common.model.Asteroid
import com.eselman.android.asteroidradar.common.model.AsteroidMapper
import com.eselman.android.asteroidradar.common.model.PictureOfDay
import com.eselman.android.asteroidradar.database.AsteroidDatabase
import com.eselman.android.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class AsteroidRepository(private val database: AsteroidDatabase) {
    val todayAsteroids: MutableLiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodayAsteroids(getCurrentDate())) {
            it.asDomainModel()
        } as MutableLiveData<List<Asteroid>>

    val allAsteroids = Transformations.map(database.asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    val weekAsteroids =
        Transformations.map(database.asteroidDao.getWeekAsteroids(getCurrentDate(), getEndDate())) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        val startDate = getCurrentDate()
        val endDate = getEndDate()
        withContext(Dispatchers.IO) {
            val asteroidsResult = RetrofitHelper.asteroidService.getAsteroids(
                startDate,
                endDate,
                BuildConfig.NASA_API_KEY
            )

            val asteroids = NetworkUtils().parseAsteroidsJsonResult(JSONObject(asteroidsResult))

            database.asteroidDao.insertAll(*(AsteroidMapper.asDatabaseModel(asteroids)))
        }
    }

    fun cleanAsteroids() {
        database.asteroidDao.deleteAsteroids()
    }

    suspend fun getPictureOfDay(): PictureOfDay {
        return withContext(Dispatchers.IO) {
            RetrofitHelper.asteroidService.getPictureOfDay(BuildConfig.NASA_API_KEY)
        }
    }

    private fun getCurrentDate() = Calendar.getInstance().time.toStringFormat()

    private fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 7)
        return calendar.time.toStringFormat()
    }
}
