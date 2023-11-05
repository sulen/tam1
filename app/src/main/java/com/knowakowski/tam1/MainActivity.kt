package com.knowakowski.tam1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.knowakowski.tam1.ui.theme.Tam1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tam1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen()
                }
            }
        }
    }
}

data class WeatherInfo(
    val city: String,
    val temperature: Double,
    val weatherDescription: String,
    val iconResource: Int
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
            Icon(
                imageVector = ImageVector.vectorResource(id = weatherInfo.iconResource),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface
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

@Preview(showBackground = true)
@Composable
fun WeatherScreen() {
    val weatherInfo = WeatherInfo(
        city = "Kraków",
        temperature = 21.1,
        weatherDescription = "Słonecznie",
        iconResource = R.drawable.sunny
    )

    WeatherCard(weatherInfo = weatherInfo)
}
