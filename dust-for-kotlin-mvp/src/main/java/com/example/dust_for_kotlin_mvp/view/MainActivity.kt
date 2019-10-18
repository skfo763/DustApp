package com.example.dust_for_kotlin_mvp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.example.dust_for_kotlin_mvp.Presenter.MainPresenter
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.model.DustModel
import com.example.dust_for_kotlin_mvp.contract.MainContract
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter = MainPresenter().also {
            it.onAttachView(this)
        }

        spinner.onItemSelectedListener {
            onItemSelected { _, _, i, _ ->
                mainPresenter.apply {
                    makeApiCall(sidoNameArr[i], 10, 1)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainPresenter.onDetachView()
    }

    override fun showError(message: String?) {
        message?.let { Log.d("TAG", message) }
    }

    override fun showToastMessage(message: String) {
        toast("Error: $message")
    }

    @SuppressLint("SetTextI18n")
    override fun uiTextSettings(dustData: DustModel, pm10Level: String, pm25Level: String) {
        air_state.text = "$pm10Level / $pm25Level"
        specific_info.text = "미세먼지 : ${dustData.pm10Value}µg / ㎡"
        specific_info_2.text = "초미세먼지 : ${dustData.pm25Value}µg / ㎡"
        specific_info_3.text = "일산화탄소 : ${dustData.coValue}µg / ㎡"
        specific_info_4.text = "오존지수 : ${dustData.o3Value}µg / ㎡"
    }

    override fun uiImageSettings(resId: Int) {
        imageview.backgroundResource = resId
    }

    override fun uiColorSettings(color: Int) {
        notification_background.backgroundColorResource = color
    }

    override fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>) {
        mainPresenter.adapter =
            ArrayAdapter(applicationContext, R.layout.simple_spinner_dropdown_item, sggNameList).apply {
                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                spinner_sgg.adapter = this
                spinner_sgg.onItemSelectedListener {
                    onItemSelected { _, _, i, _ ->
                        mainPresenter.passUiInformationForView(items[i])
                    }
                }
            }
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun getAppContext(): Context = applicationContext
}
