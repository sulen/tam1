package com.knowakowski.tam1.repository.model

data class CurrentWeather(
    val weather: List<Weather>,
    val main: MainWeather,
    val wind: WindWeather,
    val sys: SysWeather,
    val name: String,
    val id: Int,
)