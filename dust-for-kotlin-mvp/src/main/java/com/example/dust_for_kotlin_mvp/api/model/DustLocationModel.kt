package com.example.dust_for_kotlin_mvp.api.model

import com.google.gson.annotations.SerializedName

class DustLocationModel (
    @field:SerializedName("dataTime") val dataTime: String,
    @field:SerializedName("cityName") val cityName: String,
    @field:SerializedName("so2Value") val so2Value: String,
    @field:SerializedName("coValue") val coValue: String,
    @field:SerializedName("o3Value") val o3Value: String,
    @field:SerializedName("no2Value") val no2Value: String,
    @field:SerializedName("pm10Value") val pm10Value: String,
    @field:SerializedName("pm25Value") val pm25Value: String
)