package com.example.dust_for_kotlin_mvp.Presenter

import android.content.res.Resources
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
import java.net.URLEncoder

class MainPresenter: MainContract.Presenter {
    lateinit var sidoNameArr: ArrayList<String>
    private lateinit var context: MainContract.View
    private lateinit var api: DustApi
    private lateinit var dustCall: Call<DustApiResponse>
    var adapter: ArrayAdapter<String>? = null

    override fun onAttachView(view: MainContract.View) {
        this.context = view
        api = DustApiProvider.provideDustApi()
        sidoNameArr = getSidoNameArr(context.getAppContext().resources)
    }

    override fun onDetachView() {
        sidoNameArr.clear()
    }

    override fun makeApiCall(sidoName: String, numOfRow: Int, pageNo: Int) {
        dustCall = api.getDustInformation(URLEncoder.encode(sidoName, "utf-8"),
            BuildConfig.OPENAPI_CLIENT_ID, "1.0", pageNo, 100)

        dustCall.enqueue(object: Callback<DustApiResponse> {
            override fun onResponse(call: Call<DustApiResponse>, response: Response<DustApiResponse>) {
                val result = response.body()
                if(response.isSuccessful && result != null) {
                    if(result.totalCount == 0 || result.list.isEmpty()) {
                        context.showToastMessage("검색 결과가 없습니다.")
                    } else {
                        context.setSpinnerAdapter(getSggArr(result.list as ArrayList<DustModel>), result.list)
                        context.uiSettings(result.list[0])
                    }
                } else {
                    context.showError("Failure response")
                }
            }

            override fun onFailure(call: Call<DustApiResponse>, t: Throwable) {
                context.showError(t.localizedMessage)
            }
        })
    }

    private fun getSidoNameArr(resources: Resources): ArrayList<String> {
        val resArr = resources.getStringArray(R.array.sido_api)
        val result: ArrayList<String> = arrayListOf()

        for(item in resArr) {
            result.add(item)
        }
        return result
    }

    fun getSggArr(items: ArrayList<DustModel>): ArrayList<String> {
        val sggNameList: ArrayList<String> = arrayListOf()
        for(index in items) {
            sggNameList.add(index.stationName)
        }
        return sggNameList
    }
}