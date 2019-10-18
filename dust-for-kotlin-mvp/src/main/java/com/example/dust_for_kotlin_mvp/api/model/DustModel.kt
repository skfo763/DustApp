package com.example.dust_for_kotlin_mvp.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

class DustModel (

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
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(stationName)
        parcel.writeString(mangName)
        parcel.writeString(dataTme)
        parcel.writeString(so2Value)
        parcel.writeString(so2Grade)
        parcel.writeString(coValue)
        parcel.writeString(coGrade)
        parcel.writeString(o3Value)
        parcel.writeString(o3Grade)
        parcel.writeString(no2Value)
        parcel.writeString(no2Grade)
        parcel.writeString(pm10Value)
        parcel.writeString(pm10Value24)
        parcel.writeString(pm10Grade)
        parcel.writeString(pm25Value)
        parcel.writeString(pm25Value24)
        parcel.writeString(pm25Grade)
        parcel.writeString(khaiValue)
        parcel.writeString(khaiGrade)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DustModel> {
        override fun createFromParcel(parcel: Parcel): DustModel {
            return DustModel(parcel)
        }

        override fun newArray(size: Int): Array<DustModel?> {
            return arrayOfNulls(size)
        }
    }

}