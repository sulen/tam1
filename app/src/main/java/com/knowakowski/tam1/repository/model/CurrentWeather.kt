package com.knowakowski.tam1.repository.model

data class CurrentWeather(
    val weather: List<Weather>,
    val main: MainWeather,
    val name: String,
)