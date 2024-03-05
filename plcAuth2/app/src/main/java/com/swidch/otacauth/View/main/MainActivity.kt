package com.swidch.otacauth.View.main

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.ssenstone.swidchauthsdk.SwidchAuthSDK
import com.ssenstone.swidchauthsdk.constants.SwidchAuthSDKError
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.View.setting.AccountSettingActivity
import com.swidch.otacauth.View.setting.ChangePinFragment
import com.swidch.otacauth.View.setting.SecurityFragment
import com.swidch.otacauth.View.setting.TermsActivity
import com.swidch.otacauth.databinding.MainIncludeDrawerBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding:MainIncludeDrawerBinding
    private var step = OTP
    private var mSwidchAuthSDK: SwidchAuthSDK? = null
    private var useStatus: String? = null
    private var confirmState = false
    var qrState = QR_TATE_SCAN
    var cmAlertDialog: CMAlertDialog ? = null

    private fun initLibrary(): Boolean {
        mSwidchAuthSDK = SwidchAuthSDK.getInstance(this)
        if (mSwidchAuthSDK != null) {
            Log.d(QRscanFragment.TAG, "SwidchAuth Library Version: ${mSwidchAuthSDK?.sdkVersion}")
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainIncludeDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        confirmState = SharedPreferenceManager.getBooleanValue(this,SharedPreferenceHelper.KEY_STRING_AUTH_STATUS, false)
        useStatus = SharedPreferenceManager.getStringValue(this, SharedPreferenceHelper.KEY_STRING_SECURITY_STATUS)

        if (!confirmState) {
            if (useStatus == "USE_BIOMETRIC") {
                switchBiometricActivity()
            } else {
                switchPinActivity()
            }
        }
        initActionBar()
        initSideMenu()
        initLibrary()
        switchOTPFragment()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferenceManager.setBooleanValue(this@MainActivity, SharedPreferenceHelper.KEY_STRING_AUTH_STATUS, false)
    }

    override fun onBackPressed() {
        when(step) {
            ACCOUNT -> {
                switchOTPFragment()
            }
            QRSCAN -> {
                switchAccountFragment()
            }
            QRCODE ->  {
                switchAccountFragment()
            }
            OTAC -> {
                if (qrState == QR_TATE_SCAN) {
                   switchQRScanFragment()
                } else {
                    switchQRCodeFragment()
                }
            }
            NAME -> {
                // OTAC 화면으로 이동
            }
            SECURITY -> {
                switchOTPFragment()
            }
            CHANGE_PIN -> {
                 cmAlertDialog = CMAlertDialog(this, CMAlertDialog.SIMPLE_NEGATIVE_POSITIVE_ALERT, getString(R.string.alert_pin_change_cancel), getString(R.string.alert_ok_button), getString(R.string.alert_cancel_button), {
                    switchSecurityFragment()
                     cmAlertDialog?.dismiss()
                }) {cmAlertDialog?.dismiss()}
                cmAlertDialog!!.show()
            }
            OTP -> {
                finish()
            }
        }
    }

    private fun initSideMenu() {
        binding.securityLayout.setOnClickListener {
            switchSecurityFragment()
            binding.mainDrawerLayout.closeDrawer((GravityCompat.END))
        }

        binding.accountLayout.setOnClickListener {
            val intent = Intent(this, AccountSettingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            startActivity(intent)
        }

        binding.termsLayout.setOnClickListener {
            val intent = Intent(this, TermsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
            startActivity(intent)
        }
    }

    private fun switchPinActivity() {
        val intent = Intent(this, com.swidch.otacauth.View.PinActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        startActivity(intent)
    }

    private fun switchBiometricActivity() {
        val intent = Intent(this, com.swidch.otacauth.View.BiometricActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
        startActivity(intent)
    }

    fun switchOTPFragment() {
        step = OTP
//        binding.actionBar.root.visibility = View.VISIBLE
        val bundle = Bundle()
        val logo = findViewById<View>(R.id.app_logo_image)
        val setting = findViewById<View>(R.id.setting_image)
        val back = findViewById<View>(R.id.back_image)
        val title = findViewById<View>(R.id.action_bar_title)

        mFragment = OTPFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
        logo.visibility = View.VISIBLE
        setting.visibility = View.VISIBLE
        back.visibility = View.GONE
        title.visibility = View.GONE
        binding.mainDrawerLayout.setBackgroundResource(R.color.action_bar_color_1)
    }

    fun switchAccountFragment() {
        step = ACCOUNT
        val bundle = Bundle()
        val actionbar =  findViewById<View>(R.id.action_bar)
        val logo = findViewById<View>(R.id.app_logo_image)
        val setting = findViewById<View>(R.id.setting_image)
        val back = findViewById<View>(R.id.back_image)
        val title = findViewById<TextView>(R.id.action_bar_title)

        mFragment = AccountFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
        binding.mainDrawerLayout.setBackgroundResource(R.color.white)
        logo.visibility = View.GONE
        setting.visibility = View.GONE
        back.visibility = View.VISIBLE
        actionbar.visibility = View.VISIBLE
        title.visibility = View.GONE
    }

    fun switchQRScanFragment() {
        step = QRSCAN
        qrState = QR_TATE_SCAN
        val bundle = Bundle()
        val actionbar = findViewById<View>(R.id.action_bar)
        actionbar.visibility = View.GONE

        mFragment = QRscanFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchQRCodeFragment() {
        step = QRCODE
        qrState = QR_STATE_CODE
        val bundle = Bundle()
        val actionbar = findViewById<View>(R.id.action_bar)
        actionbar.visibility = View.VISIBLE

        mFragment = QRCodeFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchOTACFragment() {
        step = OTAC
        val bundle = Bundle()
        val actionbar = findViewById<View>(R.id.action_bar)
        actionbar.visibility = View.VISIBLE
        mFragment = OTACFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchAccountNameFragment() {
        step = NAME
        val bundle = Bundle()
        mFragment = AccountNameFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchEnrollSettings() {
        val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL)
        runOnUiThread() {
            startActivity(enrollIntent)
        }
    }

    private fun switchSecurityFragment() {
        step = SECURITY
        val bundle = Bundle()
        val title = findViewById<TextView>(R.id.action_bar_title)
        val logo = findViewById<View>(R.id.app_logo_image)
        val setting = findViewById<View>(R.id.setting_image)
        val back = findViewById<View>(R.id.back_image)

        title.setText(R.string.setting_bar_security_title)
        title.visibility = View.VISIBLE
        logo.visibility = View.GONE
        setting.visibility = View.GONE
        back.visibility = View.VISIBLE
        binding.mainDrawerLayout.setBackgroundResource(R.color.white)
        mFragment = SecurityFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchSecurityPinChange() {
        step = CHANGE_PIN
        val bundle = Bundle()
        val title = findViewById<TextView>(R.id.action_bar_title)
        title.text = getString(R.string.pin_title_text)
        mFragment = ChangePinFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    private fun initActionBar() {
        val setting = findViewById<View>(R.id.setting_image)
        val back = findViewById<View>(R.id.back_image)

        back.setOnClickListener {
            onBackPressed()
        }

        setting.setOnClickListener {
            val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            manager!!.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            binding.mainDrawerLayout.openDrawer((GravityCompat.END))
        }
    }

    fun confirmQrRegistration(qrCode: String): Boolean {
        val confirm = mSwidchAuthSDK?.confirmOtacRegistrationPLC(qrCode, null, null, true)
        return confirm == SwidchAuthSDKError.NO_ERROR
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private var mFragment: Fragment? = null

        const val OTP = 0
        const val ACCOUNT = 1
        const val SECURITY = 2
        const val QRSCAN = 3
        const val QRCODE = 4
        const val OTAC = 5
        const val NAME = 6
        const val CHANGE_PIN = 7

        const val QR_TATE_SCAN = 12
        const val QR_STATE_CODE = 13
    }
}