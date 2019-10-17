package com.example.dust_for_kotlin.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dust_for_kotlin.BuildConfig
import com.example.dust_for_kotlin.R
import com.example.dust_for_kotlin.api.DustApi
import com.example.dust_for_kotlin.api.DustApiProvider
import com.example.dust_for_kotlin.api.model.DustApiResponse
import com.example.dust_for_kotlin.api.model.DustModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URLEncoder

class MainActivity : AppCompatActivity() {

    var sidoAPI: String? = null
    lateinit var sidoApiArr: ArrayList<String>
    lateinit var api:DustApi
    lateinit var dustCall: Call<DustApiResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api = DustApiProvider.provideDustApi()

        val resources: Resources = resources
        sidoApiArr = getAPISidoArr(resources)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                sidoAPI = sidoApiArr[position]
                makeApiCall(sidoAPI!!, 10, 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                toast("아이템이 선택되지 않았습니다.")
            }
        }
    }

    private fun makeApiCall(sidoAPI: String, numOfRow: Int, pageNo: Int) {
        dustCall = api.getDustInformation(URLEncoder.encode(sidoAPI, "utf-8"),
            BuildConfig.OPENAPI_CLIENT_ID, "1.0", pageNo, 100)
        println(dustCall.request().url)
        dustCall.enqueue(object: Callback<DustApiResponse> {
            override fun onResponse(call: Call<DustApiResponse>, response: Response<DustApiResponse>) {
                val result = response.body()
                if(response.isSuccessful && result != null) {
                    if(result.totalCount == 0 || result.list.isEmpty()) {
                        showToastMessage("검색 결과가 없습니다.")
                    } else {
                        spinnerSettings(result.list as ArrayList<DustModel>)
                        uiSettings(result.list[0])
                    }
                } else {
                    showError("Failure response")
                }
            }

            override fun onFailure(call: Call<DustApiResponse>, t: Throwable) {
                showError(t.localizedMessage)
            }
        })
    }

    private fun spinnerSettings(items: ArrayList<DustModel>) {
        val sggNameList = getSggArr(items)
        setSpinnerAdapter(sggNameList, items)
    }

    private fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>) {
        val adapter = ArrayAdapter<String>(applicationContext, R.layout.simple_spinner_dropdown_item, sggNameList)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        spinner_sgg.adapter = adapter
        spinner_sgg.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                uiSettings(items[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uiSettings(dustData: DustModel) {
        specfic_info.text = "미세먼지 : ${dustData.pm10Value}"
        specfic_info_2.text = "초미세먼지 : ${dustData.pm25Value}"
        specfic_info_3.text = "일산화탄소 : ${dustData.coValue}"
        specfic_info_4.text = "오존지수 : ${dustData.o3Value}"
    }

    private fun getAPISidoArr(resources: Resources): ArrayList<String> {
        val resArr = resources.getStringArray(R.array.sido_api)
        val result: ArrayList<String> = arrayListOf()

        for(item in resArr) {
            result.add(item)
        }
        return result
    }

    private fun showError(message: String?) {
        if (message != null) {
            Log.d("TAG", message)
        }
    }

    private fun getSggArr(items: ArrayList<DustModel>): ArrayList<String> {
        val sggNameList: ArrayList<String> = arrayListOf()
        for(index in items) {
            sggNameList.add(index.stationName)
        }
        return sggNameList
    }

    private fun showToastMessage(message: String) {
        toast("Error: $message")
    }
}
