package com.uth.weatherapp

import android.content.Context
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class OpenMeteoResponse(
    val latitude: Double,
    val longitude: Double,
    val current_weather: CurrentWeatherData
)

@Serializable
data class CurrentWeatherData(
    val temperature: Float,
    val weathercode: Int
)

data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)

object WeatherCache {
    private const val PREF_NAME = "weather_cache"
    private const val KEY_JSON = "cached_json"
    private const val KEY_TIME = "cached_time"
    private const val CACHE_VALIDITY_MS = 5 * 60 * 1000L // 5 phút

    fun save(context: Context, data: OpenMeteoResponse) {
        val jsonString = Json.encodeToString(OpenMeteoResponse.serializer(), data)
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_JSON, jsonString)
            .putLong(KEY_TIME, System.currentTimeMillis())
            .apply()
    }

    fun getValidCache(context: Context): OpenMeteoResponse? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_JSON, null) ?: return null
        val saveTime = prefs.getLong(KEY_TIME, 0L)

        val age = System.currentTimeMillis() - saveTime
        if (age > CACHE_VALIDITY_MS) return null

        return try {
            Json.decodeFromString(OpenMeteoResponse.serializer(), jsonString)
        } catch (e: Exception) {
            null
        }
    }
}

object WeatherApi {
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    val defaultCities = listOf(
        City("Hà Nội", 21.0285, 105.8542),
        City("TP. Hồ Chí Minh", 10.8231, 106.6297),
        City("Đà Nẵng", 16.0544, 108.2022),
        City("Tokyo (Nhật)", 35.6762, 139.6503),
        City("New York (Mỹ)", 40.7128, -74.0060),
        City("Paris (Pháp)", 48.8566, 2.3522)
    )

    suspend fun fetchWeather(lat: Double, lon: Double): OpenMeteoResponse {
        return client.get("https://api.open-meteo.com/v1/forecast") {
            parameter("latitude", lat)
            parameter("longitude", lon)
            parameter("current_weather", true)
        }.body()
    }
}