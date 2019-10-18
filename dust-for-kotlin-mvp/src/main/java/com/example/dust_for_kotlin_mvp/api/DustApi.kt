package com.example.dust_for_kotlin_mvp.api

import com.example.dust_for_kotlin_mvp.BuildConfig
import com.example.dust_for_kotlin_mvp.api.model.DustApiResponse
import com.example.dust_for_kotlin_mvp.api.model.DustLocationResponse
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

    @GET("ArpltnInforInqireSvc/getCtprvnMesureSidoLIst")
    fun getDustInfoByLocation(
        @Query("sidoName", encoded = true) sidoName: String,
        @Query("ServiceKey", encoded = true) key: String = BuildConfig.OPENAPI_CLIENT_ID,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 50,
        @Query("searchCondition") condition: String = "DAILY",
        @Query("_returnType") type: String = "json"
    ): Call<DustLocationResponse>
}