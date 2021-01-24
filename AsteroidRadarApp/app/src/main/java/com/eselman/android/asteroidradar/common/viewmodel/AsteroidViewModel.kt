package com.eselman.android.asteroidradar.common.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.eselman.android.asteroidradar.common.model.Asteroid
import com.eselman.android.asteroidradar.common.model.PictureOfDay
import com.eselman.android.asteroidradar.database.getDatabase
import com.eselman.android.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AsteroidViewModel(application: Application) : AndroidViewModel(application) {
    private val _viewModelJob = SupervisorJob()

    private val _viewModelScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    private val _database = getDatabase(application)

    private val _asteroidRepository = AsteroidRepository(_database)

    val pictureOfDay = MutableLiveData<PictureOfDay>()

    private val _todayAsteroids: MutableLiveData<List<Asteroid>> = _asteroidRepository.todayAsteroids

    private val _allAsteroids = _asteroidRepository.allAsteroids

    private val _weekAsteroids = _asteroidRepository.weekAsteroids

    val asteroidsList = MediatorLiveData<List<Asteroid>>()

    init {
        _viewModelScope.launch {
            try {
                _asteroidRepository.refreshAsteroids()
                getTodayAsteroids()
                pictureOfDay.value = _asteroidRepository.getPictureOfDay()
            } catch (e: Exception) {
                Log.e("ASTEROIDS", "Error refreshing asteroids")
                getTodayAsteroids()
            }
        }
    }

    fun getAllAsteroids() = asteroidListSetValue(_allAsteroids)

    fun getTodayAsteroids() = asteroidListSetValue(_todayAsteroids)

    fun getWeekAsteroids() = asteroidListSetValue(_weekAsteroids)

    private fun asteroidListSetValue(source: LiveData<List<Asteroid>>) {
        asteroidsList.removeSource(source)
        asteroidsList.addSource(source) {
            asteroidsList.value = it
        }
    }

    override fun onCleared() {
        super.onCleared()
        _viewModelJob.cancel()
    }
}
