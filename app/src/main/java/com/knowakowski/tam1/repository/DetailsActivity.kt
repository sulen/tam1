package com.knowakowski.tam1.repository

import android.os.Bundle
import android.widget.Toast
import com.knowakowski.tam1.R
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import com.knowakowski.tam1.MainViewModel
import com.knowakowski.tam1.MyErrorView
import com.knowakowski.tam1.MyLoadingView
import com.knowakowski.tam1.UiState
import com.knowakowski.tam1.WeatherInfo
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailsActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val city = intent.getStringExtra("CITY")
        if (city != null) {
            viewModel.getDataForCity(city)
        }

        setContent {
            DetailsView(viewModel = viewModel)
        }
    }
}


@Composable
fun DetailsView(viewModel: MainViewModel) {
    val uiState by viewModel.immutableSingleCityWeatherData.observeAsState(UiState())

    when {
        uiState.isLoading -> {
            MyLoadingView()
        }

        uiState.error != null -> {
            MyErrorView(uiState.error!!)
        }

        uiState.data != null -> {
            uiState.data?.let {
                WeatherDetailsCard(
                    weatherInfo = WeatherDetailsInfo(
                        city = it.name,
                        temperature = it.main.temp,
                        weatherDescription = it.weather[0].main,
                        iconId = it.weather[0].icon,
                        humidity = it.main.humidity,
                        windSpeed = it.wind.speed,
                        pressure = it.main.pressure,
                        sunrise = it.sys.sunrise,
                        sunset = it.sys.sunset,
                        id = it.id.toString()
                    ),
                )
            }
        }
    }
}

@Composable
fun WeatherDetailsCard(weatherInfo: WeatherDetailsInfo) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, shape = MaterialTheme.shapes.medium),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
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
                Text(
                    text = "Humidity: ${weatherInfo.humidity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Wind Speed: ${weatherInfo.windSpeed} m/s",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Pressure: ${weatherInfo.pressure} hPa",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sunrise: ${formatTime(weatherInfo.sunrise)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Sunset: ${formatTime(weatherInfo.sunset)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

data class WeatherDetailsInfo(
    val city: String,
    val temperature: Double,
    val weatherDescription: String,
    val iconId: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int,
    val sunrise: Long,
    val sunset: Long,
    val id: String
)

fun formatTime(timeInSeconds: Long, timeZone: ZoneId = ZoneId.systemDefault()): String {
    val instant = Instant.ofEpochSecond(timeInSeconds)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
        .withZone(timeZone)
    return formatter.format(instant)
}