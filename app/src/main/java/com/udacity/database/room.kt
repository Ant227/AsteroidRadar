package com.udacity.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao{
    @Query("Select * from table_asteroid ORDER by closeApproachDate ASC")
   fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertAllAsteroids(asteroids: List<DatabaseAsteroid>)

}
@Dao
interface PicOfDayDao{
    @Query("SELECT * from table_picOfDay limit 1")
    fun getPicOfTheDay(): LiveData<DatabasePicOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfTheDay(picture: DatabasePicOfDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicOfDay::class],version = 1)
abstract class AsteroidDatabase: RoomDatabase(){
    abstract val asteroidDao: AsteroidDao
    abstract val picOfDayDao: PicOfDayDao

}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroid")
                .fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}
