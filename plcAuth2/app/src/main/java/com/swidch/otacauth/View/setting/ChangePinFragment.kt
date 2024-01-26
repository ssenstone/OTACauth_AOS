package com.swidch.otacauth.View.setting

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.swidch.otacauth.R
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.View.main.MainActivity
import com.swidch.otacauth.databinding.FragmentChangePinBinding

class ChangePinFragment: Fragment() {
    private var _binding: FragmentChangePinBinding? = null
    private val binding get() = _binding!!
    private var confirmPassword: String ? = null
    private var pinState = false
    var alertmessage1:CMAlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChangePinBinding.inflate(layoutInflater, container, false)
        val context = requireContext()
        val activity =requireActivity() as MainActivity
        var count = 0
        var newPassword = ""
        confirmPassword = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getStringValue(context, com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD)

        binding.squarePinEdit.postDelayed({
            binding.squarePinEdit.isFocusableInTouchMode = true
            binding.squarePinEdit.requestFocus()
            val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.squarePinEdit, InputMethodManager.SHOW_FORCED)
        }, 200)

        binding.pinGuideMessage.text = getString(R.string.pin_message_text)

        binding.squarePinEdit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.length > 5) {
                    if (pinState) {
                        if (newPassword.isNullOrEmpty()) {
                            newPassword = binding.squarePinEdit.text.toString()
                            binding.squarePinEdit.text?.clear()
                            binding.pinGuideMessage.text = getString(R.string.new_pin_one_more_message_text)
                        } else {
                            if (newPassword == binding.squarePinEdit.text.toString()) {
                                binding.squarePinEdit.postDelayed({
                                    binding.squarePinEdit.requestFocus()
                                    val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                                    imm.hideSoftInputFromWindow(binding.squarePinEdit.windowToken, 0)
                                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD, binding.squarePinEdit.text.toString())
                                    activity.switchOTPFragment()
                                }, 30)

                            } else {
                                alertmessage1 = CMAlertDialog(context, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_new_pin_failed), getString(R.string.alert_ok_button)) {
                                    binding.squarePinEdit.text?.clear()
                                    binding.pinGuideMessage.text = getString(R.string.new_pin_message_text)
                                    newPassword = ""
                                    alertmessage1?.dismiss()
                                }
                                alertmessage1?.show()
                            }
                        }
                    } else {
                        if (s.toString() == confirmPassword) {
                            pinState = true
                            binding.squarePinEdit.text?.clear()
                            binding.pinGuideMessage.text = getString(R.string.new_pin_message_text)
                            binding.warningText.visibility = View.GONE
                        } else {
                            count += 1
                            binding.warningText.text = getString(R.string.pin_warning_message, count, 5)
                            binding.warningText.visibility = View.VISIBLE
                            binding.squarePinEdit.text?.clear()
                            if(count == 3) {
                                alertmessage1 = CMAlertDialog(context, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_warning_message), getString(R.string.alert_ok_button)){alertmessage1?.dismiss()}
                                alertmessage1?.show()
                            }
                            if (count == 5) {
                                binding.warningText.visibility = View.GONE
                                alertmessage1 = CMAlertDialog(context, CMAlertDialog.SIMPLE_POSITIVE_ALERT, getString(R.string.alert_pin_faild_message), getString(R.string.alert_ok_button)) {
                                    binding.squarePinEdit.postDelayed({
                                        binding.squarePinEdit.requestFocus()
                                        val imm = context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                                        imm.hideSoftInputFromWindow(binding.squarePinEdit.windowToken, 0)
                                    }, 30)
                                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setChangeAccountList(context, null)
                                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setStringValue(context, com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper.KEY_STRING_PIN_PASSWORD, null)
                                    val intent = Intent(context, com.swidch.otacauth.View.PinActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                                    startActivity(intent)
                                    activity.finish()
                                    alertmessage1?.dismiss()
                                }
                                alertmessage1?.show()
                            }

                        }
                    }
                }
            }
        })

        return binding.root
    }
}