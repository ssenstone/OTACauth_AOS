package com.swidch.otacauth.View.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.poovam.pinedittextfield.BuildConfig
import com.swidch.otacauth.databinding.FragmentSettingTermsCategoryBinding

class TermsCategoryFragment:Fragment() {
    private var _binding: FragmentSettingTermsCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingTermsCategoryBinding.inflate(layoutInflater, container, false)
        val activity = requireActivity() as TermsActivity
        val context = requireContext()
        binding.termsOfServiceButton.setOnClickListener {
            activity.switchTermsOfService()
        }
        binding.versionName.text = context.packageManager.getPackageInfo(context.packageName, 0).versionName
        return binding.root
    }
}