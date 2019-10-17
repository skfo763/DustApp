package com.example.dust_for_kotlin_mvp.api.model

import com.google.gson.annotations.SerializedName

class DustApiResponse (
    @field:SerializedName("totalCount") val totalCount: Int,
    val list: List<DustModel>
)