package com.example.dust_for_kotlin_mvp.Presenter

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.dust_for_kotlin_mvp.BuildConfig
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.DustApiProvider
import com.example.dust_for_kotlin_mvp.api.model.DustLocationModel
import com.example.dust_for_kotlin_mvp.api.model.DustLocationResponse
import com.example.dust_for_kotlin_mvp.contract.MainContract
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException

class LocationManager: MainContract.CustomLocationManager {
    private val convertApi = DustApiProvider.provideDustApi()
    private lateinit var convertCall: Call<DustLocationResponse>
    private lateinit var locationManager: LocationManager
    private lateinit var presenter: MainPresenter

    override fun onLinked(pre: MainPresenter, view: MainContract.View, lm: LocationManager) {
        this.presenter = pre
        this.presenter.context = view
        this.locationManager = lm
    }

    override fun onDisLinked() { }

    override fun onLocationButtonClicked() {
        if(getPermissionState()) {
            presenter.context.requestPermission()
        } else {
            getCoordinates()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCoordinates() {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            presenter.context.moveOnToGpsSettings()
        } else {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).apply {
                if(this == null) {
                    Toast.makeText(presenter.context.getAppContext(), "GPS 정보를 받아올 수 없습니다. 잠시 뒤 실행해 주세요", Toast.LENGTH_SHORT).show()
                } else {
                    searchLocation(longitude, latitude)
                }
            }
        }
    }

    override fun searchLocation(cordX: Double, cordY: Double) {
        "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc".toHttpUrlOrNull()?.newBuilder()?.let {
            it.addEncodedQueryParameter("coords", "$cordX,$cordY")
            it.addEncodedQueryParameter("output", "json")
        }?.build().toString().let {
            val request = Request.Builder()
                .url(it)
                .header("X-NCP-APIGW-API-KEY-ID", BuildConfig.NAVERMAP_CERT_ID)
                .header("X-NCP-APIGW-API-KEY", BuildConfig.NAVERMAP_CERT_SECRET)
                .build()

            OkHttpClient().apply {
                newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: okhttp3.Call, e: IOException) { }

                    override fun onResponse(call: okhttp3.Call, response: Response) {
                        if(response.isSuccessful) {
                            val obj = JSONObject(response.body!!.string())
                            val sido = obj.getJSONArray("results").getJSONObject(1).getJSONObject("region")
                                .getJSONObject("area1").get("name").toString()
                            val sgg = obj.getJSONArray("results").getJSONObject(1).getJSONObject("region")
                                .getJSONObject("area2").get("name").toString()
                            retrieveLocationDust(getSidoName(sido, presenter.context.getAppContext().resources), sgg)
                        }
                    }
                })
            }
        }
    }

    fun retrieveLocationDust(sido: String, sgg: String) {
        convertCall = convertApi.getDustInfoByLocation(sido)
        convertCall.enqueue(object: retrofit2.Callback<DustLocationResponse> {
            override fun onFailure(call: Call<DustLocationResponse>, t: Throwable) { }

            override fun onResponse(call: Call<DustLocationResponse>, response: retrofit2.Response<DustLocationResponse>) {
                val result = response.body()
                presenter.context.hideProgress()
                
                if(response.isSuccessful && result != null) {
                    if(result.totalCount == 0 || result.items.isEmpty()) {
                        presenter.context.showToastMessage("검색 결과가 없습니다.")

                    } else {
                        getArrIndex(sgg, result.items).apply {
                            val levelPm10 = presenter.getLevelPm10(this.pm10Value)
                            val levelPm25 = presenter.getLevelPm25(this.pm25Value)

                            presenter.context.uiTextSettingsForLoc(this, levelPm10, levelPm25, sido)
                            presenter.context.uiColorSettings(presenter.getColor(levelPm10))
                            presenter.context.uiImageSettings(presenter.getImage(levelPm10))
                        }
                    }
                } else {
                    presenter.context.hideProgress()
                    presenter.context.showError("Failure response")
                }
            }
        })
    }

    private fun getPermissionState(): Boolean {
        return (ActivityCompat.checkSelfPermission(presenter.context.getAppContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(presenter.context.getAppContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
    }

    private fun getArrIndex(sgg: String, data: List<DustLocationModel>): DustLocationModel {
        for(index in data) {
            if(index.cityName == sgg) {
                return index
            }
        }
        return data[0]
    }

    private fun getSidoName(sido: String, res: Resources): String {
        val resArr = res.getStringArray(R.array.sidos)
        val sidoArr = res.getStringArray(R.array.sido_api)
        return sidoArr[resArr.indexOf(sido)] ?: "서울"
    }
}