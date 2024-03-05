package com.swidch.otacauth.View

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.databinding.ActivityLandingBinding
import java.util.concurrent.Executor

class BiometricActivity:AppCompatActivity() {
    private lateinit var binding: ActivityLandingBinding

    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: BiometricPrompt.PromptInfo? = null

    private var count = 0
    private var useState: String? = null

    private lateinit var cmAlertDialog: CMAlertDialog

    private val accessLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d(TAG, "registerForActivityResult - result : $result")

        if (result.resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "registerForActivityResult - RESULT_OK")
            authenticateToEncrypt()  //생체 인증 가능 여부확인 다시 호출
        } else {
            Log.d(TAG, "registerForActivityResult - NOT RESULT_OK")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        useState = SharedPreferenceManager.getStringValue(this, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS)
        biometricPrompt = setBiometricPrompt()
        promptInfo = setPromptInfo()

        binding.biometricButton.setOnClickListener {
            authenticateToEncrypt()
        }

        authenticateToEncrypt()
    }

    override fun onBackPressed() {
        //super.onBackPressed();
    }

    private fun setPromptInfo(): BiometricPrompt.PromptInfo {
        val promptBuilder: BiometricPrompt.PromptInfo.Builder = BiometricPrompt.PromptInfo.Builder()

        promptBuilder.setTitle("Biometric login for my app")
        promptBuilder.setSubtitle("Log in using your biometric credential")
        promptBuilder.setNegativeButtonText("Cancel")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            promptBuilder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        }

        promptInfo = promptBuilder.build()
        return promptInfo as PromptInfo
    }

    private fun setBiometricPrompt(): BiometricPrompt {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor!!, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@BiometricActivity, "$errString".trimIndent(), Toast.LENGTH_LONG).show()
                if (errorCode == 7) {
                    if (useState =="USE_BIOMETRIC") {
                        if (count == 4) {
                            val intent = Intent(this@BiometricActivity, PinActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        if (count == 4) {
                            cmAlertDialog = CMAlertDialog(this@BiometricActivity, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_faild_message), getString(R.string.alert_ok_button)) {
                                resetApplication()
                                cmAlertDialog?.dismiss()
                            }

                            cmAlertDialog.show()
                            resetApplication()
                        }
                    }
                }
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d(TAG, "지문 인식 성공")
                SharedPreferenceManager.setBooleanValue(this@BiometricActivity, SharedPreferenceHelper.KEY_STRING_AUTH_STATUS, true)
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@BiometricActivity, getString(R.string.failed_finger_print), Toast.LENGTH_SHORT).show()
                count += 1
                if (useState =="USE_BIOMETRIC") {
                    if (count == 5) {
                        val intent = Intent(this@BiometricActivity, PinActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    if (count == 5) {
                        cmAlertDialog = CMAlertDialog(this@BiometricActivity, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_faild_message), getString(R.string.alert_ok_button)) {
                            resetApplication()
                            cmAlertDialog?.dismiss()
                        }

                         cmAlertDialog.show()
                        resetApplication()
                    }
                }
            }
        })

        return biometricPrompt as BiometricPrompt
    }

    private fun resetApplication() {
        SharedPreferenceManager.setStringValue(this, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_PIN")
        SharedPreferenceManager.setAccountList(this, null)
        SharedPreferenceManager.setStringValue(this, SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD, null)
        finish()
    }

    private fun authenticateToEncrypt() {
        Log.d(TAG, "authenticateToEncrypt() ")

        val biometricManager = BiometricManager.from(this@BiometricActivity)

        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            // 생체 인증 사용가능
            BiometricManager.BIOMETRIC_SUCCESS -> {
                goAuthenticate()
            }
            // 기기에서 생체 인증을  지원하지 않는 경우
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {  }
            // 생체 인증 정보가 등록되어 있지 않은 경우
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                cmAlertDialog = CMAlertDialog(this, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_no_bioinfo_message), getString(R.string.alert_ok_button)) {
                    goBiometricSettings()
                    cmAlertDialog.dismiss()
                }
                cmAlertDialog.show()
            }
        }
    }

    private fun goAuthenticate() {
        Log.d(TAG, "goAuthenticate - promptInfo: $promptInfo")
        promptInfo?.let {
            biometricPrompt?.authenticate(it)
        }
    }

    private fun goBiometricSettings() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
            putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BIOMETRIC_STRONG)
        }
        accessLauncher.launch(enrollIntent)
    }

    companion object {
        private val TAG = BiometricActivity::class.java.simpleName

    }
}