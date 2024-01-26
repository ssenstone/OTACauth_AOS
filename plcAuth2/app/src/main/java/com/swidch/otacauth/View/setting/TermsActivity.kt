package com.swidch.otacauth.View.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.swidch.otacauth.R
import com.swidch.otacauth.View.BiometricActivity
import com.swidch.otacauth.View.main.MainActivity
import com.swidch.otacauth.databinding.ActivitySettingTermsOfServiceBinding

class TermsActivity:AppCompatActivity() {
    private lateinit var binding: ActivitySettingTermsOfServiceBinding
    var step = CATEGORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingTermsOfServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchCategory()
        initActionBar()
    }

    override fun onBackPressed() {
        when(step) {
           CATEGORY -> {
               val intent = Intent(this, MainActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
               startActivity(intent)
           }
           TERMS_OF_SERVICE -> {switchCategory()}
        }
    }

    private fun initActionBar() {
        val backButton = findViewById<View>(R.id.setting_back_image)
        val actionButton = findViewById<View>(R.id.setting_action_button)
        actionButton.visibility = View.GONE
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

   fun switchTermsOfService() {
        step = TERMS_OF_SERVICE
        val bundle = Bundle()
        val settingTitle = findViewById<TextView>(R.id.setting_bar_title)
        settingTitle.text = getString(R.string.button_terms_of_service)
        mFragment = TermsOfServiceFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

   fun switchCategory() {
        step = CATEGORY
        val bundle = Bundle()
        val settingTitle = findViewById<TextView>(R.id.setting_bar_title)
        settingTitle.text = getString(R.string.setting_terms_of_service)
        mFragment = TermsCategoryFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    companion object {
        private val TAG = TermsActivity::class.java.simpleName
        private var mFragment: Fragment? = null

        const val CATEGORY = "CATEGORY"
        const val TERMS_OF_SERVICE = "TERMS_OF_SERVICE"
        const val PRIVACY_POLICY = "PRIVACY_POLICY"
    }
}