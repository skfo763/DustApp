package com.example.dust_for_kotlin_mvp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.dust_for_kotlin_mvp.Presenter.MainPresenter
import com.example.dust_for_kotlin_mvp.R
import com.example.dust_for_kotlin_mvp.api.model.DustLocationModel
import com.example.dust_for_kotlin_mvp.api.model.DustModel
import com.example.dust_for_kotlin_mvp.contract.MainContract
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener

class MainActivity : AppCompatActivity(), MainContract.View {
    private lateinit var mainPresenter: MainPresenter
    private val reqCode: Int = 1000
    private var tempData: DustModel? = null

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

        search_location.setOnClickListener {
            showProgress()
            mainPresenter.locationManager.onLocationButtonClicked()
        }

        go_specificActivity.setOnClickListener {
            if(tempData != null) {
                Intent(this@MainActivity, SpecificActivity::class.java)
                    .putExtra("data", tempData).apply {
                        startActivity(this)
                    }

            } else {
                Toast.makeText(this, "위치 기반 서비스는 상세 정보를 제공하지 않고 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        goToSpinnerLayout.setOnClickListener {
            locationLayout.visibility = View.GONE
            spinnerLayout.visibility = View.VISIBLE
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

    override fun moveOnToGpsSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        startActivity(intent)
    }

    override fun setNetworkAlert() {
        alert(title="인터넷 연결 알림", message="인터넷이 연결되지 않아 서비스를 이용하실 수 없습니다. 설정 창으로 이동하시겠습니까?") {
            positiveButton("확인") {
                Intent(Settings.ACTION_WIFI_SETTINGS).apply {
                    startActivityForResult(this, 0)
                }
            }
            negativeButton("취소") {
                this@MainActivity.finish()
            }
        }.show()
    }

    @SuppressLint("SetTextI18n")
    override fun uiTextSettings(dustData: DustModel, pm10Level: String, pm25Level: String) {
        tempData = dustData
        locationLayout.visibility = View.GONE
        spinnerLayout.visibility = View.VISIBLE

        air_state.text = "$pm10Level / $pm25Level"
        specific_info.text = "미세먼지 : ${dustData.pm10Value}µg / ㎡"
        specific_info_2.text = "초미세먼지 : ${dustData.pm25Value}µg / ㎡"
        specific_info_3.text = "일산화탄소 : ${dustData.coValue}µg / ㎡"
        specific_info_4.text = "오존지수 : ${dustData.o3Value}µg / ㎡"
    }

    @SuppressLint("SetTextI18n")
    override fun uiTextSettingsForLoc(dustData: DustLocationModel, pm10Level: String, pm25Level: String, sido: String) {
        tempData = null
        spinnerLayout.visibility = View.GONE
        locationLayout.visibility = View.VISIBLE

        sidoText.text = sido
        sggText.text = dustData.cityName
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

    override fun requestPermission() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            reqCode
        )
    }

    override fun setSpinnerAdapter(sggNameList: ArrayList<String>, items: ArrayList<DustModel>) {
        mainPresenter.adapter =
            ArrayAdapter(applicationContext, R.layout.simple_spinner_dropdown_item, sggNameList
            ).apply {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == reqCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "승인이 허가되어 있습니다.", Toast.LENGTH_LONG).show()
                mainPresenter.locationManager.onLocationButtonClicked()
            } else {
                Toast.makeText(this, "아직 승인받지 않았습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK) {
            mainPresenter.onAttachView(this)

        } else if(resultCode != RESULT_OK) {
            toast("네트워크 연결을 확인하시고 앱을 다시 실행해주세요")
            this.finish()
        }
    }

    override fun getAppContext(): Context = applicationContext

    override fun getSystemServ(): ConnectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getLocationServ(): android.location.LocationManager =
        getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
}
