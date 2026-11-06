package com.example.a08_data_binding

import com.google.gson.JsonParser

data class Weather(val city: String, val temperature: Double, val description: String) {
    companion object {
        fun fromJson(json: String): Weather {
            val root = JsonParser.parseString(json).asJsonObject
            val weather = Weather(
                city = root["name"].asString,
                temperature = root.getAsJsonObject("main")["temp"].asDouble,
                description = root.getAsJsonArray("weather")[0].asJsonObject["description"].asString,
            )
            require(weather.city.isNotBlank() && weather.description.isNotBlank())
            require(weather.temperature.isFinite())
            return weather
        }
    }
}
