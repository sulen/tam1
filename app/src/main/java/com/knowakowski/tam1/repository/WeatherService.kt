package com.knowakowski.tam1.repository

import com.knowakowski.tam1.repository.model.CurrentWeather
import com.knowakowski.tam1.repository.model.CurrentWeatherGroup
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/data/2.5/weather")
    suspend fun getCurrentWeatherForCity(
        @Query("q") city: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeather>

    @GET("/data/2.5/group")
    suspend fun getCurrentWeather(
        @Query("id") cityIds: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String = API_KEY
    ): Response<CurrentWeatherGroup>

    companion object {
        private const val STAR_URL = "https://api.openweathermap.org"
        private const val API_KEY = "43a876d47e5dccc4e5894e51a240f2c7"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(STAR_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val weatherService: WeatherService by lazy {
            retrofit.create(WeatherService::class.java)
        }
    }
}