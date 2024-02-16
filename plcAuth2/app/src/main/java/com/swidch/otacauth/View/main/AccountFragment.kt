package com.swidch.otacauth.View.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.swidch.otacauth.databinding.FragmentAccountBinding
import com.ssenstone.swidchauthsdk.SwidchAuthSDK
import java.lang.Exception

class AccountFragment: Fragment() {
    private var _binding: FragmentAccountBinding ? = null
    private val binding get() = _binding!!
    private var mSwidchAuthSDK: SwidchAuthSDK? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        val context = requireContext()
        val activity = requireActivity() as MainActivity
        mSwidchAuthSDK = SwidchAuthSDK.getInstance(context)

        generateUserKey()

        binding.reLoadButton.setOnClickListener {
            generateUserKey()
        }

        binding.qrscanButton.setOnClickListener {

            activity.switchQRScanFragment()
        }

        binding.qrcodeButton.setOnClickListener {
            activity.switchQRCodeFragment()
        }

        return binding.root
    }

    private fun generateUserKey() {

        try {
            val nonce = mSwidchAuthSDK?.generateNonce()
            if (!nonce.isNullOrEmpty()) {
                binding.key1.text = nonce[0].toString()
                binding.key2.text = nonce[1].toString()
                binding.key3.text = nonce[2].toString()
                binding.key4.text = nonce[3].toString()
                binding.key5.text = nonce[4].toString()
                binding.key6.text = nonce[5].toString()
            }

        } catch (e:Exception) {

            Log.e("error", e.toString())
        }
    }

}