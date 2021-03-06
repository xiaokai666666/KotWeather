package com.example.kotweather.network

import com.example.kotweather.common.Constant
import com.example.kotweather.model.Daily
import com.example.kotweather.model.HourlyData
import com.example.kotweather.model.RealTime
import com.example.kotweather.module.searchplace.model.SearchPlaceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    /**
     * 仔细观察，我们在searchPlaces()方法上面声明了一个@GET注解，这样当调用searchPlaces()方法的时候，
     * Retrofit会自动发起一条GET请求，去访问@GET注解中配置的地址。其中，搜索城市数据的API中只有query这个参数
     * 是需要动态指定的，我们使用@Query注解的方式来实现。定义好接口后，为了使用它，还需要创建一个Retrofit构建器RetrofitFactory.kt
     */
    @GET("v2/place?token=${Constant.CAIYUN_TOKEN}&lang=zh_CN")
    suspend fun searchPlaces(
            @Query("query") query: String
    ): SearchPlaceResponse

    @GET("v2.5/${Constant.CAIYUN_TOKEN}/{lng},{lat}/realtime.json")
    suspend fun loadRealtimeWeather(
            @Path("lng") lng: String?,
            @Path("lat") lat: String?
    ): RealTime

    @GET("v2.5/${Constant.CAIYUN_TOKEN}/{lng},{lat}/daily.json")
    suspend fun loadDailyWeather(
            @Path("lng") lng: String?,
            @Path("lat") lat: String?
    ): Daily

    @GET("v2.5/${Constant.CAIYUN_TOKEN}/{lng},{lat}/hourly.json?hourlysteps=12")
    suspend fun loadHourlyWeather(
            @Path("lng") lng: String?,
            @Path("lat") lat: String?
    ): HourlyData
}