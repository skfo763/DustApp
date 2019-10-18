package com.example.dust_for_kotlin_mvp.contract

import android.content.Context

interface SpecificContract {

    interface View {
        fun showError(message: String?)

        fun showToastMessage(message: String)

        fun getAppContext(): Context

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {
        fun onAttachView(view: MainContract.View)

        fun onDetachView()
    }
}