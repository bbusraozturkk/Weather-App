package com.example.weatherapp.view

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.mrcaracal.havadurumumrc.viewmodel.MainViewModel

lateinit var edt_city_name :EditText
lateinit var ll_data : View
lateinit var tv_error : TextView
lateinit var pb_loading : ProgressBar
lateinit var tv_city_code : TextView
lateinit var tv_city_name : TextView
lateinit var tv_degree : TextView
lateinit var tv_humidity : TextView
lateinit var tv_wind_speed : TextView
lateinit var tv_lat : TextView
lateinit var tv_lon : TextView
lateinit var img_weather_pictures : ImageView
lateinit var swipe_refresh_layout : SwipeRefreshLayout


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edt_city_name = findViewById(R.id.edt_city_name)
        ll_data = findViewById(R.id.ll_data)
        tv_error = findViewById(R.id.tv_error)
        pb_loading = findViewById(R.id.pb_loading)
        tv_city_code = findViewById(R.id.tv_city_code)
        tv_city_name = findViewById(R.id.tv_city_name)
        tv_degree = findViewById(R.id.tv_degree)
        tv_humidity = findViewById(R.id.tv_humidity)
        tv_wind_speed = findViewById(R.id.tv_wind_speed)
        tv_lat = findViewById(R.id.tv_lat)
        tv_lon = findViewById(R.id.tv_lon)
        img_weather_pictures = findViewById(R.id.img_weather_pictures)
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout)


        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName", "ankara")
        edt_city_name.setText(cName)

        viewModel.refreshData(cName!!)
        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            ll_data.visibility = View.GONE
            tv_error.visibility = View.GONE
            pb_loading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.toLowerCase()
            edt_city_name.setText(cityName)
            viewModel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing = false
        }

    }

    private fun getLiveData() {
        viewModel.weather_data.observe(this, { data ->
            data?.let {
                ll_data.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
                tv_degree.text = data.main.temp.toString() + "°C"
                tv_city_code.text = data.sys.country.toString()
                tv_city_name.text = data.name.toString()
                tv_humidity.text = data.main.humidity.toString() + "%"
                tv_wind_speed.text = data.wind.speed.toString()
                tv_lat.text = data.coord.lat.toString()
                tv_lon.text = data.coord.lon.toString()


                Glide.with(this)
                    .load("https://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(img_weather_pictures)


            }
        })
        viewModel.weather_loading.observe(this, Observer { loading ->
            loading?.let {

                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }

            }
        })
        viewModel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    tv_error.visibility = View.VISIBLE
                    pb_loading.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    tv_error.visibility = View.GONE
                }
            }

        })
    }
}