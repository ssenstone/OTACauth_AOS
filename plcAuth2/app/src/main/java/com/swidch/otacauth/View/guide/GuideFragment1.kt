package com.swidch.otacauth.View.guide

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swidch.otacauth.databinding.FragmentGuide01Binding

class GuideFragment1: Fragment() {
    private var _binding : FragmentGuide01Binding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGuide01Binding.inflate(layoutInflater, container, false)
        val activity = requireActivity() as GuideActivity

        return binding.root
    }
}