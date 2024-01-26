package com.swidch.otacauth.View.main

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.swidch.otacauth.databinding.FragmentAccountQrScanBinding
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.swidch.otacauth.R
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class QRscanFragment:Fragment() {
    private var _binding:FragmentAccountQrScanBinding? = null
    private val binding get() = _binding!!
    private var cmAlertDialog: CMAlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAccountQrScanBinding.inflate(layoutInflater, container, false)

        val context = requireContext()
        val activity = requireActivity() as MainActivity

        // 카메라 권한 확인
        val cameraPermissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) { // 권한이 없는 경우
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(android.Manifest.permission.CAMERA),
                1000
            )
        }

        init()

        binding.qrcodeButton.setOnClickListener { activity.switchQRCodeFragment() }

        binding.backButton.setOnClickListener{
            activity.switchAccountFragment()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) { //거부 }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.viewBarcode.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.viewBarcode.pause()
    }

    private fun init() {
        disableLoger()
        binding.viewBarcode.run {
            barcodeView.decoderFactory = DefaultDecoderFactory(mutableListOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39))
            val activity = requireActivity()as MainActivity
            initializeFromIntent(activity.intent)
            decodeContinuous {
                try {
                    Log.i(TAG, "message = ${it.text}")

                    val result = it.text
                    val jsonObject = JSONObject(result)
                    val confirm = activity.confirmQrRegistration(result)

                    if (confirm) {
                        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setQRdata(context, jsonObject.toString())

                        val userId = jsonObject.getString("userId")
                        val systemId = jsonObject.getString("systemId")
                        val svrDeviceId = jsonObject.getString("svrDeviceId")
                        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, "userId", userId)
                        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, "systemId", systemId)
                        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, "svrDeviceId", svrDeviceId)
                        activity.switchOTACFragment()
                    } else {
                        cmAlertDialog = CMAlertDialog(context,CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_qr_failed_message), getString(R.string.alert_ok_button)) {
                            activity.switchAccountFragment()
                            cmAlertDialog?.dismiss()
                        }
                        cmAlertDialog?.show()
                        binding.viewBarcode.pause()
                    }

                } catch (e: JSONException) {
                    Log.e(TAG, "message = ${it.text}")
                }
            }
        }
    }

    private fun  disableLoger() {
        val viewFinder = binding.viewBarcode.viewFinder
        try {
            val scannerField = viewFinder.javaClass.getDeclaredField("SCANNER_ALPHA")
            scannerField.isAccessible = true
            scannerField.set(viewFinder, intArrayOf(1))
        } catch (e: Exception) { }
    }


    companion object {
        val TAG: String = QRscanFragment::class.java.simpleName
    }
}