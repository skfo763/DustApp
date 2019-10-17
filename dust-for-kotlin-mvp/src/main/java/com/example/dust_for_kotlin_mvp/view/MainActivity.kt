package com.example.dust_for_kotlin_mvp.view

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.dust_for_kotlin_mvp.Presenter.MainPresenter
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.model.DustModel
import com.example.dust_for_kotlin_mvp.contract.MainContract
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainPresenter = MainPresenter().also {
            it.onAttachView(this)
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                toast("아이템이 선택되지 않았습니다.")
            }
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mainPresenter.sidoNameArr[position].apply {
                    mainPresenter.makeApiCall(this, 10, 1)
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
    override fun uiSettings(dustData: DustModel) {
        specfic_info.text = "미세먼지 : ${dustData.pm10Value}"
        specfic_info_2.text = "초미세먼지 : ${dustData.pm25Value}"
        specfic_info_3.text = "일산화탄소 : ${dustData.coValue}"
        specfic_info_4.text = "오존지수 : ${dustData.o3Value}"
    }

    override fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>) {
        mainPresenter.adapter =
            ArrayAdapter(applicationContext, R.layout.simple_spinner_dropdown_item, sggNameList).apply {
                setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                spinner_sgg.adapter = this
            }

        spinner_sgg.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                uiSettings(items[p2])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    override fun getAppContext(): Context = applicationContext
}
