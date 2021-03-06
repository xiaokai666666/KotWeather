package com.example.kotweather.module.home.repository

import androidx.lifecycle.MutableLiveData
import com.example.kotweather.base.repository.ApiRepository
import com.example.kotweather.common.RoomHelper
import com.example.kotweather.common.state.State

class HomeDetailRepository(var loadState: MutableLiveData<State>): ApiRepository() {

    suspend fun queryAllPlace() = RoomHelper.queryAllPlaces(loadState)

    suspend fun loadRealtimeWeather(lng: String?, lat: String?) =
            apiService.loadRealtimeWeather(lng, lat)

    suspend fun loadDailyWeather(lng: String?, lat: String?) =
            apiService.loadDailyWeather(lng, lat)

    suspend fun loadHourlyWeather(lng: String?, lat: String?) =
            apiService.loadHourlyWeather(lng, lat)

    suspend fun updateChoosePlace(temperature: Int, skycon: String, name: String) =
            RoomHelper.updateChoosePlace(temperature, skycon, name)
}