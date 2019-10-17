package com.example.dust_for_kotlin.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

class DustModel(

    @field:SerializedName("stationName") val stationName: String,
    @field:SerializedName("mangName") val mangName: String,
    @field:SerializedName("dataTme") val dataTme: String,

    @field:SerializedName("so2Value") val so2Value: String,
    @field:SerializedName("so2Grade") val so2Grade: String,

    @field:SerializedName("coValue") val coValue: String,
    @field:SerializedName("coGrade") val coGrade: String,

    @field:SerializedName("o3Value") val o3Value: String,
    @field:SerializedName("o3Grade") val o3Grade: String,

    @field:SerializedName("no2Value") val no2Value: String,
    @field:SerializedName("no2Grade") val no2Grade: String,

    @field:SerializedName("pm10Value") val pm10Value: String,
    @field:SerializedName("pm10Value24") val pm10Value24: String,
    @field:SerializedName("pm10Grade") val pm10Grade: String,

    @field:SerializedName("pm25Value") val pm25Value: String,
    @field:SerializedName("pm25Value24") val pm25Value24: String,
    @field:SerializedName("pm25Grade") val pm25Grade: String,

    @field:SerializedName("khaiValue") val khaiValue: String,
    @field:SerializedName("khaiGrade") val khaiGrade: String
)