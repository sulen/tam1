package com.knowakowski.tam1.repository



class WeatherRepository {
    suspend fun getCurrentWeatherForCity(city: String) =
        WeatherService.weatherService.getCurrentWeatherForCity(city = city)

    suspend fun getCurrentWeather() =
        WeatherService.weatherService.getCurrentWeather(cityIds = "3094802,6695624,3099434")
}