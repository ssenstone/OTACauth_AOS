package com.swidch.otacauth.View.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.swidch.otacauth.View.adpter.SettingAccountListAdapter
import com.swidch.otacauth.databinding.FragmentSettingAccountListBinding

class SettingAccountListFragment(): Fragment() {
    private var _binding: FragmentSettingAccountListBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingAccountListBinding.inflate(layoutInflater, container, false)
        val settingAccountListAdapter = SettingAccountListAdapter()
        val accountListString = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getAccountList(requireContext())
        val gson = GsonBuilder().create()

        if (!accountListString.isNullOrEmpty()) {
            val accountList = gson.fromJson(accountListString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
            val accountArrayList = ArrayList<com.swidch.otacauth.Model.AccountItem>()
            accountArrayList.addAll(accountList)

            binding.accountListRecyclerView.adapter = settingAccountListAdapter
            binding.accountListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            settingAccountListAdapter.setSettingAccountList(accountArrayList, binding.accountListRecyclerView)
        }

        return binding.root
    }
}