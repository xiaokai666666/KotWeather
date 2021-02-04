package com.example.kotweather.module.chooseplace.view

import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.kotweather.R
import com.example.kotweather.base.view.BaseLifeCycleFragment
import com.example.kotweather.common.Constant
import com.example.kotweather.common.util.SPreference
import com.example.kotweather.module.chooseplace.adapter.ChoosePlaceAdapter
import com.example.kotweather.module.chooseplace.viewModel.ChoosePlaceViewModel
import kotlinx.android.synthetic.main.custom_bar.view.*
import kotlinx.android.synthetic.main.fragment_list.*
import com.example.kotweather.databinding.FragmentListBinding
import com.example.kotweather.model.Place


class ChoosePlaceFragment: BaseLifeCycleFragment<ChoosePlaceViewModel, FragmentListBinding>() {

    private lateinit var mAdapter: ChoosePlaceAdapter

    private lateinit var mHeaderView: View

    private var mPosition: Int by SPreference(Constant.POSITION, 0)

    private var mPlaceList = arrayListOf<Place>()

    override fun getLayoutId() = R.layout.fragment_list

    override fun initView() {
        super.initView()
//        showSuccess()
        initAdapter()
        initHeaderView()
    }

    override fun initData() {
        super.initData()
        mViewModel.queryAllPlace()
    }

    override fun initDataObserver() {
        mViewModel.mPlaceData.observe(this, Observer { response ->
            response?.let {
                setPlaceList(response)
            }
        })
    }

    private fun initHeaderView() {
        mHeaderView = View.inflate(requireActivity(), R.layout.custom_bar, null)
        mHeaderView.apply {
            detail_title.text = "添加的城市"
            detail_start.visibility = View.VISIBLE
            detail_end.visibility = View.VISIBLE
            detail_end.setOnClickListener {
                Navigation.findNavController(it).navigate(
                        R.id.action_choosePlaceFragment_to_searchPlaceFragment)
            }
            detail_start.setOnClickListener {
                Navigation.findNavController(it).navigateUp()
            }
        }
        mAdapter.addHeaderView(mHeaderView)
    }

    private fun initAdapter() {
        mAdapter = ChoosePlaceAdapter(R.layout.place_item, null)
        mRvArticle?.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRvArticle.adapter = mAdapter
        // 这里长按的话，只起删除作用，还需要修改
        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            val place = mAdapter.getItem(position)
            place?.let {
                mViewModel.deletePlace(place)
                mAdapter.notifyDataSetChanged()
            }
            true
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
//            var bundle = Bundle()
//            appViewModel.changeCurrentPlace(mAdapter.getItem(position))
            mPosition = position
//            bundle.putString("lng", mViewModel.mPlaceData.value?.get(position)?.location?.lng)
//            bundle.putString("lat", mViewModel.mPlaceData.value?.get(position)?.location?.lat)
//            bundle.putString("placeName", mAdapter.getItem(position).name)
            Navigation.findNavController(view).navigateUp()
        }
    }


    private fun setPlaceList(addedPlaceList: MutableList<Place>) {
        mAdapter.setNewInstance(addedPlaceList)
    }
}