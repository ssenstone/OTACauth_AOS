package com.swidch.otacauth.View.setting

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.View.main.MainActivity
import com.swidch.otacauth.databinding.FragmentSettingSecurityBinding

class SecurityFragment:Fragment() {
    private var _binding: FragmentSettingSecurityBinding? = null
    private val binding get() = _binding!!
    private var useStatus:String? = null

    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null
    private lateinit var cmAlertDialog: CMAlertDialog

    private val accessLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("SecurityFragment", "registerForActivityResult - result : $result")

        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("SecurityFragment", "registerForActivityResult - RESULT_OK")
        } else {
            Log.d("SecurityFragment", "registerForActivityResult - NOT RESULT_OK")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingSecurityBinding.inflate(layoutInflater,container,false)
        val activity = requireActivity() as MainActivity
        val context = requireContext()
        useStatus = SharedPreferenceManager.getStringValue(context,SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS)

        if (useStatus == "USE_ALL") {
            binding.useAllCheckbox.isChecked = true
        } else if (useStatus == "USE_BIOMETRIC") {
            binding.useAllCheckbox.isChecked = false
            binding.securitySiwtch.isChecked = true
        } else {
            binding.useAllCheckbox.isChecked = false
            binding.securitySiwtch.isChecked = false
        }

        binding.securitySiwtch.textOff = getString(R.string.switch_pin)
        binding.securitySiwtch.textOn = getString(R.string.switch_other)
        binding.securitySiwtch.showText = true
        binding.securitySwitchCloseLayout.visibility = View.VISIBLE
        binding.securitySwitchClose
        binding.pinchange.setOnClickListener {
            activity.switchSecurityPinChange()
        }

        binding.useAllCheckbox.setOnCheckedChangeListener(object :OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    val biometricManager = BiometricManager.from(context)

                    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                        // 생체 인증 사용가능
                        BiometricManager.BIOMETRIC_SUCCESS -> {
                            SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_ALL")
                        }
                        // 기기에서 생체 인증을  지원하지 않는 경우
                        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                            binding.useAllCheckbox.isChecked = false
                            binding.securitySwitchLayout.visibility = View.VISIBLE
                            binding.securitySwitchCloseLayout.visibility = View.GONE
                        }
                        // 생체 인증 정보가 등록되어 있지 않은 경우
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                            cmAlertDialog = CMAlertDialog(context, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_no_bioinfo_message), getString(R.string.alert_ok_button)) {
                                activity.switchEnrollSettings()
                                binding.useAllCheckbox.isChecked = false
                                binding.securitySwitchLayout.visibility = View.VISIBLE
                                binding.securitySwitchCloseLayout.visibility = View.GONE
                                cmAlertDialog.dismiss()
                            }
                            cmAlertDialog.show()
                        }
                    }
                } else {
                    if (binding.securitySiwtch.isChecked) {
                        SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_BIOMETRIC")
                    } else {
                        SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_PIN")
                    }
                }
            }
        })

        binding.securitySiwtch.setOnCheckedChangeListener(object : OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    val biometricManager = BiometricManager.from(context)

                    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                        // 생체 인증 사용가능
                        BiometricManager.BIOMETRIC_SUCCESS -> {
                            SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_BIOMETRIC")
                        }
                        // 기기에서 생체 인증을  지원하지 않는 경우
                        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                            binding.securitySiwtch.isChecked = false
                            binding.trackOn.visibility = View.GONE
                            binding.trackOff.visibility = View.VISIBLE
                        }
                        // 생체 인증 정보가 등록되어 있지 않은 경우
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                            cmAlertDialog = CMAlertDialog(context, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_no_bioinfo_message), getString(R.string.alert_ok_button)) {
                                activity.switchEnrollSettings()
                                binding.securitySiwtch.isChecked = false
                                binding.trackOn.visibility = View.GONE
                                binding.trackOff.visibility = View.VISIBLE
                                cmAlertDialog.dismiss()
                            }
                            cmAlertDialog.show()
                        }
                    }

                } else {
                    SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_PIN")
                }
            }
        })

        if (binding.useAllCheckbox.isChecked) {
            binding.securitySwitchLayout.visibility = View.GONE
            binding.securitySwitchCloseLayout.visibility = View.VISIBLE
            SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_ALL")
        } else {
            binding.securitySwitchLayout.visibility = View.VISIBLE
            binding.securitySwitchCloseLayout.visibility = View.GONE
            if (binding.securitySiwtch.isChecked) {
                SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_BIOMETRIC")
            } else {
                SharedPreferenceManager.setStringValue(context, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_PIN")
            }
        }

        if (binding.securitySiwtch.isChecked) {
            binding.trackOn.visibility = View.VISIBLE
            binding.trackOff.visibility = View.GONE
        } else {
            binding.trackOn.visibility = View.GONE
            binding.trackOff.visibility = View.VISIBLE
        }

        binding.useAllCheckbox.setOnClickListener {
            if (binding.useAllCheckbox.isChecked) {
                binding.securitySwitchLayout.visibility = View.GONE
                binding.securitySwitchCloseLayout.visibility = View.VISIBLE
            } else {
                binding.securitySwitchLayout.visibility = View.VISIBLE
                binding.securitySwitchCloseLayout.visibility = View.GONE
            }
        }

        binding.securitySiwtch.setOnClickListener {
            if (binding.securitySiwtch.isChecked) {
                binding.trackOn.visibility = View.VISIBLE
                binding.trackOff.visibility = View.GONE
            } else {
                binding.trackOn.visibility = View.GONE
                binding.trackOff.visibility = View.VISIBLE
            }
        }

        binding.enrollButton.setOnClickListener {
            activity.switchEnrollSettings()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val context = requireContext()
        val biometricManager = BiometricManager.from(context)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            // 생체 인증 사용가능
            BiometricManager.BIOMETRIC_SUCCESS -> {
                binding.enrollButton.setText(R.string.delete_button)
            }
            // 기기에서 생체 인증을  지원하지 않는 경우
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                binding.enrollButton.setText(R.string.use_button)
            }
            // 생체 인증 정보가 등록되어 있지 않은 경우
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                binding.enrollButton.setText(R.string.use_button)
            }
        }
    }
}