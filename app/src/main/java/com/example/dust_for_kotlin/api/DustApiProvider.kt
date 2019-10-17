package com.example.dust_for_kotlin.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object DustApiProvider {
    fun provideDustApi(): DustApi {
        return Retrofit.Builder()
            .baseUrl("http://openapi.airkorea.or.kr/openapi/services/rest/")
            .client(getEncodedClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DustApi::class.java)
    }

    private fun getEncodedClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    internal class AuthInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
        }
    }
}
