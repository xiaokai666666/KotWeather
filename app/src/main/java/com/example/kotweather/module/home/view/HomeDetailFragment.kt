package com.example.kotweather.module.home.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotweather.R
import com.example.kotweather.base.view.BaseLifeCycleFragment
import com.example.kotweather.common.Constant
import com.example.kotweather.common.util.getAirLevel
import com.example.kotweather.common.util.getSky
import com.example.kotweather.common.util.getWindOri
import com.example.kotweather.common.util.getWindSpeed
import com.example.kotweather.databinding.HomeDetailFragmentBinding
import com.example.kotweather.model.Daily
import com.example.kotweather.model.HourlyWeather
import com.example.kotweather.model.RealTime
import com.example.kotweather.module.home.adapter.HomeDailyAdapter
import com.example.kotweather.module.home.adapter.HourlyAdapter
import com.example.kotweather.module.home.viewmodel.HomeDetailViewModel
import kotlinx.android.synthetic.main.home_detail_fragment.*
import kotlinx.android.synthetic.main.layout_container.*
import kotlinx.android.synthetic.main.layout_current_place_detail.*
import kotlinx.android.synthetic.main.layout_flipper_detail.*
import kotlinx.android.synthetic.main.life_index.*


class HomeDetailFragment: BaseLifeCycleFragment<HomeDetailViewModel, HomeDetailFragmentBinding>() {

    private lateinit var mAdapterHome: HomeDailyAdapter

    private lateinit var mAdapterHourly: HourlyAdapter

    private val mLng: String by lazy { arguments?.getString(Constant.LNG_KEY) ?: "" }

    private val mLat: String by lazy { arguments?.getString(Constant.LAT_KEY) ?: "" }

    private val mPlaceName: String by lazy { arguments?.getString(Constant.PLACE_NAME) ?: "" }

    var list = ArrayList<HourlyWeather>()

    companion object{
        fun newInstance(placeName: String, lng: String, lat: String, placeKey: Int): Fragment {
            val bundle = Bundle()
            bundle.putString(Constant.LNG_KEY, lng)
            bundle.putString(Constant.LAT_KEY, lat)
            bundle.putString(Constant.PLACE_NAME, placeName)
            bundle.putInt(Constant.PLACE_PRIVATE_KEY, placeKey)
            val fragment = HomeDetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.home_detail_fragment

    override fun initView() {
        super.initView()
        initRefresh()
        setHasOptionsMenu(true)
        initAdapter()
    }

    override fun initData() {
        if (home_container.isRefreshing) {
            home_container.isRefreshing = false
            list.clear()
        }
        mViewModel.loadRealtimeWeather(mLng, mLat)
        mViewModel.loadDailyWeather(mLng, mLat)
        mViewModel.loadHourlyWeather(mLng, mLat)
    }

    override fun initDataObserver() {
        super.initDataObserver()

        mViewModel.mRealtimeData.observe(this, Observer { response ->
            response?.let {
                initCurrentData(it.result.realtime)
                mViewModel.updateChoosePlace(
                        it.result.realtime.temperature.toInt(),
                        it.result.realtime.skycon,
                        mPlaceName
                )
            }
        })

        mViewModel.mDailyData.observe(this, Observer { response ->
            response?.let {
                val dailyDataList = ArrayList<Daily.DailyData>()
                for (i in 0 until it.result.daily.skycon.size) {
                    dailyDataList.add(
                        Daily.DailyData(
                            it.result.daily.skycon[i].date,
                            it.result.daily.skycon[i].value,
                            it.result.daily.temperature[i].max,
                            it.result.daily.temperature[i].min
                        )
                    )
                }
                initDailyData(dailyDataList)
                initDailyIndex(it.result.daily.life_index)
            }
        })

        /**
         * val temp: Double,
         * val skycon: Skycon,
         * val weather: String,
         * val time: String,
         * val weatherImg: Int,
         * val windOri: String,
         * val windLevel: String,
         * val airLevel: String
         */
        mViewModel.mHourlyData.observe(this, Observer { response ->
            response?.let {
                for (i in 0 until it.result.hourly.temperature.size) {
                    list.add(
                        HourlyWeather(
                                it.result.hourly.temperature[i].value.toInt(),
                                it.result.hourly.skycon[i],
                                getSky(it.result.hourly.skycon[i].value).info,
                                it.result.hourly.temperature[i].datetime.substring(11, 16),
                                getSky(it.result.hourly.skycon[i].value).icon,
                                getWindOri(it.result.hourly.wind[i].direction).ori,
                                getWindSpeed(it.result.hourly.wind[i].speed).speed,
                                getAirLevel(it.result.hourly.air_quality.aqi[i].value.chn).airLevel)
                    )
                }
                initHourlyView(list)
            }
        })
        showSuccess()
    }

    private fun initRefresh() {
        // 设置下拉刷新新的loading颜色
        home_container.setProgressBackgroundColorSchemeColor(
                ContextCompat.getColor(requireContext(), R.color.material_blue)
        )
        mDataBinding.homeDetailViewModel = mViewModel
        home_container.setColorSchemeColors(Color.WHITE)
        home_container.setOnRefreshListener { initData() }
    }

    private fun initDailyIndex(lifeIndex: Daily.LifeIndex) {
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
    }

    private fun initDailyData(dailyData: MutableList<Daily.DailyData>) {
        mAdapterHome.setNewInstance(dailyData)
    }

    private fun initHourlyView(list: ArrayList<HourlyWeather>){
        mAdapterHourly.setNewInstance(list)
    }

    @SuppressLint("ResourceType")
    private fun initCurrentData(realtime: RealTime.Realtime) {
        temp_text_view.text = "${realtime.temperature.toInt()} ℃"
        description_text_view.text = getSky(
            realtime.skycon
        ).info
        animation_view.setImageResource(
            getSky(
                realtime.skycon
            ).icon
        )
        humidity_text_view.text = "湿度: ${(realtime.humidity * 100).toInt()}"
        wind_text_view.text = "风力: ${getWindSpeed(realtime.wind.speed).speed}"
        visible_text_view.text = "能见度: ${realtime.visibility} m"
        index_text_view.text = "空气指数: ${getAirLevel(realtime.air_quality.aqi.chn).airLevel}"
    }

    private fun initAdapter() {
        mAdapterHome = HomeDailyAdapter(R.layout.daily_item, null)
        home_recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        home_recycler.adapter = mAdapterHome

        mAdapterHourly = HourlyAdapter(R.layout.hourly_item, null)
        hourly_recycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        hourly_recycler.adapter = mAdapterHourly

    }
}