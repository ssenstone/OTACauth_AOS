package com.swidch.otacauth.View.main

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ssenstone.swidchauthsdk.SwidchAuthSDK
import com.ssenstone.swidchauthsdk.constants.SwidchAuthSDKError
import com.swidch.otacauth.databinding.FragmentAccountOtacBinding
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OTACFragment: Fragment() {
    private var _binding: FragmentAccountOtacBinding ?= null
    private val binding get() = _binding!!

    private var userId:String? = null
    private var systemId:String? = null
    private var svrDeviceId:String? = null

    private var mSwidchAuthSDK: SwidchAuthSDK? = null

    private lateinit var countDownTimer: CountDownTimer

    private fun initLibrary(): Boolean {
        val context = requireContext()
        mSwidchAuthSDK = SwidchAuthSDK.getInstance(context)
        if (mSwidchAuthSDK != null) {
            Log.d(QRscanFragment.TAG, "SwidchAuth Library Version: ${mSwidchAuthSDK?.sdkVersion}")
        }
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountOtacBinding.inflate(layoutInflater, container, false)
        val context = requireContext()
        val activity = requireActivity() as MainActivity

        userId = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getStringValue(context, "userId")
        systemId = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getStringValue(context, "systemId")
        svrDeviceId = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getStringValue(context, "svrDeviceId")


        initLibrary()
        initCountDownTimer()
        showOTAC(userId, systemId, svrDeviceId)

        binding.completeButton.setOnClickListener {
            activity.switchAccountNameFragment()
        }

        return binding.root
    }

    private fun initCountDownTimer() {
        countDownTimer = object:CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                binding.timer.text = "$seconds"
            }

            override fun onFinish() {
                countDownTimer.cancel()
                showOTAC(userId, systemId, svrDeviceId)
            }
        }
    }

    private fun showOTAC(userId: String?, systemId: String?, svrDeviceId: String?) {

         mSwidchAuthSDK?.generateOtacPLC(userId!!, systemId!!, null, svrDeviceId, null, null) { i, s, s1, s2 ->
            try {
                if (i == SwidchAuthSDKError.NO_ERROR) {
                    binding.otacText1.text = s2[0].toString()
                    binding.otacText2.text = s2[1].toString()
                    binding.otacText3.text = s2[2].toString()
                    binding.otacText4.text = s2[3].toString()
                    binding.otacText5.text = s2[4].toString()
                    binding.otacText6.text = s2[5].toString()
                    binding.otacText7.text = s2[6].toString()
                    binding.otacText8.text = s2[7].toString()
                    countDownTimer.start()
                } else {
                    Log.e(TAG, "ERRORCODE : $i\n")
                }
            } catch (e: Exception) {
                Log.d("TEST_LOG", e.message!!)
            }
        }
    }

    companion object {
        private val TAG = OTACFragment::class.java.simpleName
    }
}