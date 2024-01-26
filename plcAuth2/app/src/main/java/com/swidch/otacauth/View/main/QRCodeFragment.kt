package com.swidch.otacauth.View.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swidch.otacauth.R
import com.swidch.otacauth.databinding.FragmentAccountRegistQrCodeBinding
import org.json.JSONException
import org.json.JSONObject

class QRCodeFragment:Fragment() {
    private var _binding: FragmentAccountRegistQrCodeBinding ? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentAccountRegistQrCodeBinding.inflate(layoutInflater, container, false)
        val activity = requireActivity() as MainActivity
        val context = requireContext()

        binding.qrcodeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    binding.underLineEdittext.setBackgroundResource(R.color.edit_underline_color)
                } else {
                    binding.underLineEdittext.setBackgroundResource(R.color.primary_button_background_color)
                }
            }

        })

        binding.deleteButton.setOnClickListener {
            binding.qrcodeEditText.text.clear()
        }


        binding.completeButton.setOnClickListener {
            try {
                val confirm = activity.confirmQrRegistration(binding.qrcodeEditText.text.toString())
                val jsonObject = JSONObject(binding.qrcodeEditText.text.toString())

                if (confirm) {
                    // UserId 랑 systemId 저장 후 다음 페이지
                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setQRdata(context, jsonObject.toString())

                    val userId = jsonObject.getString("userId")
                    val systemId = jsonObject.getString("systemId")
                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, "userId", userId)
                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, "systemId", systemId)
                    activity.switchOTACFragment()

                } else {
                    // 실패할 경우 경고 텍스트 출력
                    binding.editWarningMessage.visibility = View.VISIBLE
                    binding.underLineEdittext.setBackgroundResource(R.color.warning_text_color)
                }
            } catch (e: JSONException) {
                Log.e("QRCodeFragment", e.toString())
                binding.editWarningMessage.visibility = View.VISIBLE
                binding.underLineEdittext.setBackgroundResource(R.color.warning_text_color)
            }
        }

        return binding.root
    }
}