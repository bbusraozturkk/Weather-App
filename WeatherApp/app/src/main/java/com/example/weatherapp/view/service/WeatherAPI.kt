package com.example.weatherapp.view.service

import com.example.weatherapp.view.model.WeatherModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/weather?q=bing%C3%B6l&APPID=17aaa5882d51e55cf8732183ea69744c

interface WeatherAPI {

 @GET("data/2.5/weather")
 fun getData(
  @Query("q") cityName: String,
  @Query("APPID") apiKey: String = "17aaa5882d51e55cf8732183ea69744c",
  @Query("units") units: String = "metric"
 ): Single<WeatherModel>
}
