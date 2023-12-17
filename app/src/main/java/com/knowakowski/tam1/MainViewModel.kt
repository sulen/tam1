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

    private val mutableWeatherData = MutableLiveData<List<CurrentWeather>>()
    val immutableWeatherData: LiveData<List<CurrentWeather>> = mutableWeatherData

    fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = weatherRepository.getCurrentWeather()
                Log.e("MainViewModel", "Response $request", )
                val weather = request.body()?.list ?: emptyList()
                mutableWeatherData.postValue(weather)

            } catch (e: Exception) {
                Log.e("MainViewModel", "Operacja nie powiodla sie", e)
            }
        }
    }
}