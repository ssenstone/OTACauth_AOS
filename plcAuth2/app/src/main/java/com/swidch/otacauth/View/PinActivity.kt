package com.swidch.otacauth.View

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.View.main.MainActivity
import com.swidch.otacauth.databinding.ActivityPinBinding


class PinActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPinBinding
    private var confirmPassword: String ? = null
    private var confirmState: Boolean = false
    private var useState: String ? = null
    var alertmessage1: CMAlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pin = SharedPreferenceManager.getStringValue(this, com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD)
        var count = 0
        useState = SharedPreferenceManager.getStringValue(this, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS)

        if (!pin.isNullOrEmpty()) {
            confirmPassword = pin
            confirmState = true
        }

        binding.squarePinEdit.postDelayed({
            binding.squarePinEdit.isFocusableInTouchMode = true
            binding.squarePinEdit.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.squarePinEdit, InputMethodManager.SHOW_FORCED)
        }, 200)

        binding.squarePinEdit.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {

                if (s.length > 5) {

                    if (!confirmState) {
                        confirmPassword = s.toString()
                        binding.squarePinEdit.text?.clear()
                        SharedPreferenceManager.setStringValue(this@PinActivity, com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD, confirmPassword)
                        confirmState = true
                    } else {
                        if (s.toString() == confirmPassword) {
                            when(useState) {
                                "USE_ALL" -> {
                                    binding.squarePinEdit.postDelayed({
                                        binding.squarePinEdit.requestFocus()
                                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.hideSoftInputFromWindow(binding.squarePinEdit.windowToken, 0)
                                    }, 30)
                                    val intent = Intent(this@PinActivity, BiometricActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                                    startActivity(intent)
                                }
                                else -> {
                                    binding.squarePinEdit.postDelayed({
                                        binding.squarePinEdit.requestFocus()
                                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.hideSoftInputFromWindow(binding.squarePinEdit.windowToken, 0)
                                    }, 30)
                                    SharedPreferenceManager.setBooleanValue(this@PinActivity, SharedPreferenceHelper.KEY_STRING_AUTH_STATUS, true)
                                }
                            }
                            finish()
                        } else {
                            if (count != 4) {
                                count += 1
                                binding.warningText.text = getString(R.string.pin_warning_message, count, 5)
                                binding.warningText.visibility = View.VISIBLE
                                binding.squarePinEdit.text?.clear()
                                if (count == 3) {
                                    alertmessage1 = CMAlertDialog(this@PinActivity, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_warning_message), getString(R.string.alert_ok_button)){alertmessage1?.dismiss()}
                                    alertmessage1?.show()
                                }
                            } else {
                                confirmPassword = null
                                confirmState = false
                                binding.squarePinEdit.text?.clear()
                                count = 0
                                binding.warningText.visibility = View.GONE
                                alertmessage1 = CMAlertDialog(this@PinActivity, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_faild_message), getString(R.string.alert_ok_button)) {
                                    resetApplication()
                                    alertmessage1?.dismiss()
                                }

                                alertmessage1?.show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun resetApplication() {
        SharedPreferenceManager.setStringValue(this, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS, "USE_PIN")
        SharedPreferenceManager.setAccountList(this, null)
        SharedPreferenceManager.setStringValue(this, SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD, null)
        finish()
    }
}