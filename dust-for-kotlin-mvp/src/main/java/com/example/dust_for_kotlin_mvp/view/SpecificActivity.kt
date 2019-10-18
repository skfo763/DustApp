package com.example.dust_for_kotlin_mvp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.model.DustModel
import com.example.dust_for_kotlin_mvp.contract.SpecificContract

class SpecificActivity : AppCompatActivity(), SpecificContract.View {

    private var dustModel: DustModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific)

        dustModel = intent.getParcelableExtra("data")
        println(dustModel?.pm10Grade)
    }

    override fun showToastMessage(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAppContext(): Context {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideProgress() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
