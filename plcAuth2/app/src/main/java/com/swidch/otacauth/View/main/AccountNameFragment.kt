package com.swidch.otacauth.View.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.swidch.otacauth.R
import com.swidch.otacauth.databinding.FragmentAccountNameBinding
import org.json.JSONException
import org.json.JSONObject

class AccountNameFragment: Fragment() {
    private var _binding:FragmentAccountNameBinding ? = null
    private val binding get() = _binding!!
    private var accountItem: com.swidch.otacauth.Model.AccountItem = com.swidch.otacauth.Model.AccountItem()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAccountNameBinding.inflate(layoutInflater, container, false)
        val context = requireContext()
        val activity = requireActivity() as MainActivity
        val qrData  = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getQRdata(context).toString()
        val jsonObject = JSONObject(qrData)

        accountItem.svrDeviceId = jsonObject.getString("svrDeviceId")
        accountItem.userId = jsonObject.getString("userId")
        accountItem.systemId = jsonObject.getString("systemId")
        accountItem.otacPeriod = jsonObject.getInt("otacPeriod")
        try{
            if (jsonObject.getString("appAccountName").isNullOrEmpty()) {
                binding.accountNameEditText.text.clear()
            } else {
                binding.accountNameEditText.setText( jsonObject.getString("appAccountName"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        binding.completeButton.setOnClickListener {
           if (checkValidation()) {
               activity.switchOTPFragment()
           }
        }

        return binding.root
    }

    private fun checkValidation(): Boolean {
        val context = requireContext()
        val gson = GsonBuilder().create()
        val accountString = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getAccountList(context)
        val accountArrayList = ArrayList<com.swidch.otacauth.Model.AccountItem>()

        if (binding.accountNameEditText.text.isNullOrEmpty()) {
            return false
        }

        if (!accountString.isNullOrEmpty()) {
            val accountList =  gson.fromJson(accountString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
            accountArrayList.addAll(accountList)
        }  else {
            accountItem.accountName = binding.accountNameEditText.text.toString()
            accountArrayList.add(accountItem)
            val json = gson.toJson(accountArrayList)
            com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setAccountList(context, json)
            return true
        }

        for (i in accountArrayList) {
            if (i.accountName == binding.accountNameEditText.text.toString()) {
                binding.underLineEdittext.setBackgroundResource(R.color.warning_text_color)
                binding.editWarningMessage.visibility = View.VISIBLE
                binding.editInfoMessage.visibility = View.GONE
                return false
            }
        }

        accountItem.accountName = binding.accountNameEditText.text.toString()
        accountArrayList.add(accountItem)
        val json = gson.toJson(accountArrayList)
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setAccountList(context, json)
        return true
    }
}