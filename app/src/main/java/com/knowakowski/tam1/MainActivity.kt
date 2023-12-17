package com.knowakowski.tam1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.knowakowski.tam1.repository.model.CurrentWeather
import com.knowakowski.tam1.ui.theme.Tam1Theme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()

        Log.d("Main", "test2")
        setContent {
            Tam1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(viewModel = viewModel)
                }
            }
        }
    }
}

data class WeatherInfo(
    val city: String,
    val temperature: Double,
    val weatherDescription: String,
    val iconId: String,
)

@Composable
fun WeatherCard(weatherInfo: WeatherInfo) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                modifier = Modifier.size(64.dp),
                model = "https://openweathermap.org/img/w/${weatherInfo.iconId}.png",
                contentDescription = "Weather Icon",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = weatherInfo.city,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${weatherInfo.temperature}°C",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = weatherInfo.weatherDescription,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MainView(viewModel: MainViewModel) {
    val uiState by viewModel.immutableWeatherData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            MyLoadingView()
        }

        uiState.error != null -> {
            MyErrorView(uiState.error!!)
        }

        uiState.data != null -> {
            uiState.data?.let { MyListView(weatherList = it) }
        }
    }
}

@Composable
fun MyErrorView(error: String) {
    Snackbar {
        Text(text = error)
    }
}

@Composable
fun MyLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun MyListView(weatherList: List<CurrentWeather>) {
    if (weatherList.isNotEmpty()) {
        weatherList.forEachIndexed { index, weather ->
            Log.d("Main", "$index ${weather.name}, ${weather.weather[0].description}")
        }
        LazyColumn {
            items(weatherList) { weather ->
                WeatherCard(
                    weatherInfo = WeatherInfo(
                        city = weather.name,
                        temperature = weather.main.temp,
                        weatherDescription = weather.weather[0].main,
                        iconId = weather.weather[0].icon
                    )
                )
            }
        }
    }
}
