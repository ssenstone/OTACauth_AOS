package com.swidch.otacauth.View.main

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.swidch.otacauth.View.adpter.AccountListAdapter
import com.swidch.otacauth.databinding.FragmentOtpBinding


class OTPFragment: Fragment() {
    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater:LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentOtpBinding.inflate(layoutInflater, container, false)
        val activity = requireActivity() as MainActivity
        binding.addAccountButton.setOnClickListener {
            val manager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            manager!!.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            activity.switchAccountFragment()
        }

        initRecyclerSearchView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initRecyclerSearchView()
    }

    private fun initRecyclerSearchView() {
        val accountListAdapter = AccountListAdapter()
        val accountListString = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getAccountList(requireContext())
        val gson = GsonBuilder().create()

        if (!accountListString.isNullOrEmpty()) {
//            return null
            val accountList = gson.fromJson(accountListString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
            val accountArrayList = ArrayList<com.swidch.otacauth.Model.AccountItem>()
            accountArrayList.addAll(accountList)

            binding.otpListRecyclerView.adapter = accountListAdapter
            binding.otpListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            accountListAdapter.setAccountList(accountArrayList)

            binding.otpListRecyclerView.visibility = View.VISIBLE
            binding.blankImgHeight.visibility = View.GONE
            binding.blankImg.visibility = View.GONE
        } else {
            binding.otpListRecyclerView.visibility = View.GONE
            binding.blankImgHeight.visibility = View.VISIBLE
            binding.blankImg.visibility = View.VISIBLE
        }

        binding.searchOtpEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                accountListAdapter.filter.filter(query)
                return true
            }
        })
    }
}