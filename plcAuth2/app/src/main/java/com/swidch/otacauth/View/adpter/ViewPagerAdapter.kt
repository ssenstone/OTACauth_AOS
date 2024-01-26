package com.swidch.otacauth.View.adpter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.swidch.otacauth.View.guide.GuideActivity
import com.swidch.otacauth.View.guide.GuideFragment1
import com.swidch.otacauth.View.guide.GuideFragment2

class ViewPagerAdapter(guideActivity: GuideActivity): FragmentStateAdapter(guideActivity) {
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val fragments = listOf<Fragment>(GuideFragment1(), GuideFragment2())

    override fun getItemCount(): Int {return fragments.size}

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}