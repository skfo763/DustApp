package com.example.dust_for_kotlin_mvp.contract

import android.content.Context
import android.net.ConnectivityManager
import com.example.dust_for_kotlin_mvp.api.model.DustModel

interface MainContract {
    interface View {
        fun showError(message: String?)

        fun showToastMessage(message: String)

        fun uiTextSettings(dustData: DustModel, pm10Level: String, pm25Level: String)

        fun uiImageSettings(resId: Int)

        fun uiColorSettings(color: Int)

        fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>)

        fun getAppContext(): Context

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {
        fun onAttachView(view: View)

        fun onDetachView()

        fun makeApiCall(sidoName: String, numOfRow: Int, pageNo: Int)

        fun checkInternet()

        fun isConnected()

        fun requestPermission()

        fun searchLocation()

        fun setAddress()
    }
}