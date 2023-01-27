package com.udacity.asteroidradar.main

import android.app.Application

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.database.AsteroidDatabase
import com.udacity.database.getDatabase
import kotlinx.coroutines.launch
import java.lang.Exception


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database: AsteroidDatabase = getDatabase(application)
    private val asteroidRepository = AsteroidsRepository(database)

    val asteroidListFromData = asteroidRepository.asteroidListFromData
    val pictureOfTheDayFromData = asteroidRepository.pictureOfTheDayFromData

    init {

            viewModelScope.launch {
                try {
                    asteroidRepository.refreshAsteroids()
                    asteroidRepository.refreshPicOfDay()
                }
                catch (e: Exception){
                    Log.e("MainViewModel","Error")
                }

            }


    }


}















