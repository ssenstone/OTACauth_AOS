package com.swidch.otacauth.View.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.databinding.FragmentGuide02Binding

class GuideFragment2: Fragment() {
    private var _binding : FragmentGuide02Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGuide02Binding.inflate(layoutInflater, container, false)
        val activity = requireActivity() as GuideActivity
        val context = requireContext()

        return binding.root
    }
}