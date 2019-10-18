package com.example.dust_for_kotlin_mvp.contract

import android.content.Context
import android.net.ConnectivityManager
import com.example.dust_for_kotlin_mvp.Presenter.LocationManager
import com.example.dust_for_kotlin_mvp.Presenter.MainPresenter
import com.example.dust_for_kotlin_mvp.api.model.DustLocationModel
import com.example.dust_for_kotlin_mvp.api.model.DustModel

interface MainContract {
    interface View {
        fun showError(message: String?)

        fun showToastMessage(message: String)

        fun uiTextSettings(dustData: DustModel, pm10Level: String, pm25Level: String)

        fun uiTextSettingsForLoc(dustData: DustLocationModel, pm10Level: String, pm25Level: String, sido: String)

        fun uiImageSettings(resId: Int)

        fun uiColorSettings(color: Int)

        fun setNetworkAlert()

        fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>)

        fun getAppContext(): Context

        fun requestPermission()

        fun getSystemServ(): ConnectivityManager

        fun showProgress()

        fun hideProgress()

        fun getLocationServ(): android.location.LocationManager

        fun moveOnToGpsSettings()
    }

    interface Presenter {
        fun onAttachView(view: View)

        fun onDetachView()

        fun makeApiCall(sidoName: String, numOfRow: Int, pageNo: Int)

        fun checkInternet(view: View)

        fun isConnected(): Boolean
    }

    interface CustomLocationManager {
        fun onLinked(pre: MainPresenter, view: View, lm: android.location.LocationManager)

        fun onDisLinked()

        fun onLocationButtonClicked()

        fun searchLocation(cordX: Double, cordY: Double)
    }
}