package com.ace.rainbender.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ace.rainbender.ui.view.TodayFragment
import com.ace.rainbender.ui.view.TomorrowFragment

class TabAdapter ( fm: FragmentManager, internal var totalTabs: Int, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                TodayFragment()
            }
            1 -> {
                TomorrowFragment()
            }
            else -> TodayFragment()
        }
    }

    override fun getItemCount(): Int {
        return totalTabs
    }
}