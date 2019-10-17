package com.example.dust_for_kotlin_mvp.contract

import android.content.Context
import com.example.dust_for_kotlin_mvp.api.model.DustModel

interface MainContract {
    interface View {
        fun showError(message: String?)

        fun showToastMessage(message: String)

        fun uiSettings(dustData: DustModel)

        fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>)

        fun getAppContext(): Context
    }

    interface Presenter {
        fun onAttachView(view: MainContract.View)

        fun onDetachView()

        fun makeApiCall(sidoName: String, numOfRow: Int, pageNo: Int)
    }
}