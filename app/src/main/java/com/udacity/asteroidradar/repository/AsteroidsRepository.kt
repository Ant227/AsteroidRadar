package com.udacity.asteroidradar.repository


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidService
import com.udacity.asteroidradar.api.PictureOfDayService
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.database.AsteroidDatabase
import com.udacity.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*


class AsteroidsRepository(val database: AsteroidDatabase){

    private val apiKey = "yoa2r8vKJtZzoqSKI4Nnc3RdeTc3TeE8RdrKBZwq"
    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    private val calendar = Calendar.getInstance()
    private val currentTime = calendar.time
    private val startDate = dateFormat.format(currentTime)

val pictureOfTheDayFromData: LiveData<PictureOfDay> = Transformations.map(database.picOfDayDao.getPicOfTheDay()) {
    it?.let {
        it.asDomainModel()
    }
}
val asteroidListFromData: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroids()) {
    it?.asDomainModel()
}

    suspend fun refreshAsteroids(){
        withContext(Dispatchers.IO){
            try {
                calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
                val endDate = dateFormat.format(calendar.time)
                val asteroidList = AsteroidService.AsteroidApi.retrofitService.getAsteroidList(
                    startDate, endDate, apiKey
                )
                val asteroidParsed = parseAsteroidsJsonResult(JSONObject(asteroidList))


                val asteroidDatabaseVersion = asteroidParsed.asDatabaseModel()

                database.asteroidDao.insertAllAsteroids(asteroidDatabaseVersion)
            }
            catch (e: UnknownHostException){
                Log.e("AsteroidsRepository", "no network error")
            }

        }
    }

    suspend fun refreshPicOfDay(){
        withContext(Dispatchers.IO) {
            try {


                val pictureOfTheDay = PictureOfDayService.PictureOfDayApi
                    .retrofitServicePic.getPictureOfTheDay(apiKey)

                val picOfDayDatabase = pictureOfTheDay.asDatabaseModel()
                database.picOfDayDao.insertPictureOfTheDay(picOfDayDatabase)
            }
            catch (e : UnknownHostException){
                Log.e("AsteroidsRepository", "no network error")
            }
        }
    }


}

