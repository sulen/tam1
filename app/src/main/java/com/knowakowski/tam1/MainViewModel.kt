package com.knowakowski.tam1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knowakowski.tam1.repository.WeatherRepository
import com.knowakowski.tam1.repository.model.CurrentWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val weatherRepository = WeatherRepository()

    private val mutableWeatherData = MutableLiveData<UiState<List<CurrentWeather>>>()
    val immutableWeatherData: LiveData<UiState<List<CurrentWeather>>> = mutableWeatherData

    private val mutableSingleCityWeatherData = MutableLiveData<UiState<CurrentWeather>>()
    val immutableSingleCityWeatherData: LiveData<UiState<CurrentWeather>> = mutableSingleCityWeatherData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutableWeatherData.postValue(UiState(isLoading = true))
                val request = weatherRepository.getCurrentWeather()
                val weather = request.body()?.list ?: emptyList()

                mutableWeatherData.postValue(UiState(data = weather))
            } catch (e: Exception) {
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
                mutableWeatherData.postValue(UiState(error = e.message))
            }
        }
    }

    fun getDataForCity(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                mutableSingleCityWeatherData.postValue(UiState(isLoading = true))
                val request = weatherRepository.getCurrentWeatherForCity(city)
                val weather = request.body()

                if (weather != null) {
                    mutableSingleCityWeatherData.postValue(UiState(data = weather))
                } else {
                    mutableSingleCityWeatherData.postValue(UiState(error = "Brak danych"))
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
                mutableSingleCityWeatherData.postValue(UiState(error = e.message))
            }
        }
    }
}