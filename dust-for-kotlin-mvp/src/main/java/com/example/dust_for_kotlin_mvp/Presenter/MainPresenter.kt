package com.example.dust_for_kotlin_mvp.Presenter

import android.annotation.TargetApi
import android.content.res.Resources
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Parcelable
import android.widget.ArrayAdapter
import com.example.dust_for_kotlin_mvp.BuildConfig
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.DustApi
import com.example.dust_for_kotlin_mvp.api.DustApiProvider
import com.example.dust_for_kotlin_mvp.api.model.DustApiResponse
import com.example.dust_for_kotlin_mvp.api.model.DustModel
import com.example.dust_for_kotlin_mvp.contract.MainContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.IllegalStateException
import java.net.URLEncoder

class MainPresenter: MainContract.Presenter {
    lateinit var sidoNameArr: ArrayList<String>
    internal lateinit var context: MainContract.View
    internal lateinit var locationManager: LocationManager
    private lateinit var api: DustApi
    private lateinit var dustCall: Call<DustApiResponse>
    lateinit var adapter: ArrayAdapter<String>

    override fun checkInternet(view: MainContract.View) {
        if(!isConnected()) {
            context.setNetworkAlert()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun isConnected(): Boolean {
        context.getSystemServ().apply {
            val network = this.activeNetwork
            val info = this.getNetworkCapabilities(network)
            return info?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        }
    }

    override fun onAttachView(view: MainContract.View) {
        this.context = view
        checkInternet(view)
        api = DustApiProvider.provideDustApi()
        locationManager = LocationManager().apply { onLinked(this@MainPresenter, view, context.getLocationServ()) }
        sidoNameArr = getSidoNameArr(context.getAppContext().resources)
    }

    override fun onDetachView() {
        sidoNameArr.clear()
    }

    override fun makeApiCall(sidoName: String, numOfRow: Int, pageNo: Int) {
        context.showProgress()

        dustCall = api.getDustInformation(URLEncoder.encode(sidoName, "utf-8"),
            BuildConfig.OPENAPI_CLIENT_ID, "1.0", pageNo, 100)

        dustCall.enqueue(object: Callback<DustApiResponse> {
            override fun onResponse(call: Call<DustApiResponse>, response: Response<DustApiResponse>) {
                val result = response.body()
                context.hideProgress()

                if(response.isSuccessful && result != null) {
                    if(result.totalCount == 0 || result.list.isEmpty()) {
                        context.showToastMessage("검색 결과가 없습니다.")
                    } else {
                        context.setSpinnerAdapter(getSggArr(result.list as ArrayList<DustModel>), result.list)
                        passUiInformationForView(result.list[0])
                    }
                } else {
                    context.showError("Failure response")
                }
            }

            override fun onFailure(call: Call<DustApiResponse>, t: Throwable) {
                context.showError(t.localizedMessage)
                context.hideProgress()
            }
        })
    }

    internal fun passUiInformationForView(result: DustModel) {
        val pm10Level = getLevelPm10(result.pm10Value)
        val pm25Level = getLevelPm25(result.pm25Value)
        context.uiTextSettings(result, pm10Level, pm25Level)
        context.uiColorSettings(getColor(pm10Level))
        context.uiImageSettings(getImage(pm10Level))
    }

    internal fun getImage(pm10Level: String): Int {
        return when(pm10Level) {
            "좋음" -> R.drawable.ic_sentiment_satisfied_black_24dp
            "보통" -> R.drawable.ic_sentiment_neutral_black_24dp
            "나쁨" -> R.drawable.ic_sentiment_dissatisfied_black_24dp
            "매우 나쁨" -> R.drawable.ic_sentiment_very_dissatisfied_black_24dp
            else -> R.drawable.ic_sentiment_neutral_black_24dp
        }
    }

    internal fun getColor(pm10Level: String): Int {
        return when(pm10Level) {
            "좋음" -> R.color.colorGood
            "보통" -> R.color.colorNeutral
            "나쁨" -> R.color.colorBad
            "매우 나쁨" -> R.color.colorWorst
            else -> R.color.colorUnKnown
        }
    }

    internal fun getLevelPm10(input: String): String {
        val a: Int
        return try {
            a = Integer.parseInt(input)
            when(a) {
                in 0..30 -> "좋음"
                in 31..80 -> "보통"
                in 81..150 -> "나쁨"
                in 151..200 -> "매우 나쁨"
                else -> "정보 없음"
            }
        } catch (e: Exception) {
            "정보 없음"
        }
    }

    internal fun getLevelPm25(input: String): String {
        val a: Int
        return try {
            a = Integer.parseInt(input)
            when(a) {
                in 0..15 -> "좋음"
                in 16..35 -> "보통"
                in 36..75 -> "나쁨"
                in 76..200 -> "매우 나쁨"
                else -> "정보 없음"
            }
        } catch (e: Exception) {
            "정보 없음"
        }
    }

    private fun getSidoNameArr(resources: Resources): ArrayList<String> {
        val resArr = resources.getStringArray(R.array.sido_api)
        val result: ArrayList<String> = arrayListOf()

        for(item in resArr) {
            result.add(item)
        }
        return result
    }

    private fun getSggArr(items: ArrayList<DustModel>): ArrayList<String> {
        val sggNameList: ArrayList<String> = arrayListOf()
        for(index in items) {
            sggNameList.add(index.stationName)
        }
        return sggNameList
    }
}