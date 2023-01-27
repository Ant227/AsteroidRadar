package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitMoshi = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface PictureOfDayService{

    @GET("planetary/apod")
 suspend  fun getPictureOfTheDay(
        @Query("api_key") apiKey: String
    ): NetworkPictureOfDay



    object PictureOfDayApi {

        val retrofitServicePic: PictureOfDayService by lazy {
            retrofitMoshi.create(PictureOfDayService::class.java)
        }
    }
}