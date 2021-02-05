package com.example.kotweather.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 设置适配器的列表动画
 */
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int){
    if(mode == 0){
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode-1])
    }
}


fun ViewPager.init(
        fragmentManager: FragmentManager,
        fragments: ArrayList<Fragment>
): ViewPager {
    //设置适配器
    adapter = object : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int) = fragments[position]
        override fun getCount() = fragments.size
    }
    return this
}