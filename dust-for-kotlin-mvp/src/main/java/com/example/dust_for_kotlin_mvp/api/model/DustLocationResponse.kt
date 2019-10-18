package com.example.dust_for_kotlin_mvp.api.model

import com.google.gson.annotations.SerializedName

class DustLocationResponse (
    @field:SerializedName("resultCode") val resultCode: String,
    @field:SerializedName("totalCount") val totalCount: Int,
    @field:SerializedName("list") val items: List<DustLocationModel>
)