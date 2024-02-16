package com.swidch.otacauth.View.guide

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.adpter.ViewPagerAdapter
import com.swidch.otacauth.databinding.ActivityGuideBinding

class GuideActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGuideBinding
    private var viewPagerAdapter: ViewPagerAdapter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val guideCheck =  SharedPreferenceManager.getBooleanValue(this, "GuideCheck", false)

        viewPagerAdapter = ViewPagerAdapter(this)

        binding.viewPager.adapter = viewPagerAdapter
        binding.guideButton.setText(R.string.guide_button_text)

        if (guideCheck == true) {
            switchMainActivity()
        }

        binding.guideButton.setOnClickListener {
            if(binding.viewPager.currentItem == 0) {
                switchGuide2()
                return@setOnClickListener
            } else {
                switchMainActivity()
                SharedPreferenceManager.setBooleanValue(this, "GuideCheck", true)
                return@setOnClickListener
            }

        }
    }

    private fun  switchGuide2() {
        binding.viewPager.currentItem = 1
    }

    private fun switchMainActivity() {
        val intent = Intent(this, com.swidch.otacauth.View.main.MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        startActivity(intent)
        finish()
    }

    companion object {
        private val TAG = GuideActivity::class.java.simpleName
        private var mFragment: Fragment? = null
    }
}