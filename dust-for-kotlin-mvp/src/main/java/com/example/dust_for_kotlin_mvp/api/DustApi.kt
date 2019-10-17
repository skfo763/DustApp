package com.example.dust_for_kotlin_mvp.api

import com.example.dust_for_kotlin_mvp.api.model.DustApiResponse
import retrofit2.Call
import retrofit2.http.*

interface DustApi {
    @GET("ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty")
    fun getDustInformation(
        @Query("sidoName", encoded = true) sidoName: String,
        @Query("ServiceKey", encoded = true) key: String,
        @Query("ver") ver: String = "1.0",
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 10,
        @Query("_returnType") type: String = "json"
    ): Call<DustApiResponse>
}