package com.swidch.otacauth.View.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swidch.otacauth.databinding.FragmentTermsOfServiceBinding

class TermsOfServiceFragment: Fragment() {
    private var _binding: FragmentTermsOfServiceBinding ? = null
    private val binding get()= _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTermsOfServiceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}