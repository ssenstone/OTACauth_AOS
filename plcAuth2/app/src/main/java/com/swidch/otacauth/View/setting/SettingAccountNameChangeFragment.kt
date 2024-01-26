package com.swidch.otacauth.View.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.swidch.otacauth.R
import com.swidch.otacauth.databinding.FragmentSettingAccountNameChangeBinding

class SettingAccountNameChangeFragment(private val userId: String?): Fragment() {
    private var _binding: FragmentSettingAccountNameChangeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingAccountNameChangeBinding.inflate(layoutInflater, container, false)
        val context = requireContext()
        val accountListString = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getAccountList(context)
        val gson = GsonBuilder().create()
        val accountList =  gson.fromJson(accountListString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
        val list = ArrayList<com.swidch.otacauth.Model.AccountItem>()
        list.addAll(accountList)

        val activity = requireActivity() as AccountSettingActivity

        binding.deleteButton.setOnClickListener {
            binding.accountNameEditText.text.clear()
        }

        binding.completeButton.setOnClickListener {
            for (i in list) {
                if (i.userId == userId) {
                   if (i.accountName == binding.accountNameEditText.text.toString()) {
                       binding.editWarningMessage.visibility = View.VISIBLE
                       binding.underLineEdittext.setBackgroundResource(R.color.warning_text_color)
                       binding.editInfoMessage.visibility = View.GONE
                       return@setOnClickListener
                   } else {
                       list.remove(i)
                       i.accountName = binding.accountNameEditText.text.toString()
                       list.add(i)
                       val json = gson.toJson(list)
                       com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setAccountList(context, json)
                       activity.switchAccountListFragment()
                       return@setOnClickListener
                   }
                }
            }
        }
        return binding.root
    }
}