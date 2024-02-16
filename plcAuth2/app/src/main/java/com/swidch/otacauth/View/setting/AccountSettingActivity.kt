package com.swidch.otacauth.View.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.swidch.otacauth.R
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceHelper
import com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager
import com.swidch.otacauth.View.component.Dialog.CMAlertDialog
import com.swidch.otacauth.View.main.MainActivity
import com.swidch.otacauth.databinding.ActivitySettingAccountBinding

class AccountSettingActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySettingAccountBinding
    private var cmAlertDialog: CMAlertDialog ? = null
    private var step = ACCOUNT_LIST

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initActionBar()
        switchAccountListFragment()
    }

    override fun onPause() {
        super.onPause()
        SharedPreferenceManager.setBooleanValue(this, SharedPreferenceHelper.KEY_STRING_AUTH_STATUS, true)
    }

    private fun initActionBar() {
        val back = findViewById<View>(R.id.setting_back_image)
        val actionbutton = findViewById<View>(R.id.setting_action_button)
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)

        actionbutton.setOnClickListener {
            val changeAccountListString = SharedPreferenceManager.getChangeAccountList(this)
            val gson = GsonBuilder().create()

            if (!changeAccountListString.isNullOrEmpty()) {
                val accountList = gson.fromJson(changeAccountListString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
                val accountArrayList = ArrayList<com.swidch.otacauth.Model.AccountItem>()
                accountArrayList.addAll(accountList)
                val json = gson.toJson(accountArrayList)
                SharedPreferenceManager.setAccountList(this,json)
                finish()
            } else {
                finish()
                return@setOnClickListener
            }
        }
        back.setOnClickListener {
            when (step) {
                NAME -> {
                    switchAccountListFragment()
                }
                ACCOUNT_LIST -> {
                    finish()
                }
            }
        }
    }

    fun switchAccountListFragment() {
        val bundle = Bundle()
        step = ACCOUNT_LIST
        mFragment = SettingAccountListFragment()
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    fun switchAccountNameChangeFragment(userId:String) {
        val bundle = Bundle()
        step = NAME
        val actionButton = findViewById<TextView>(R.id.setting_action_button)
        val accountListString = com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.getAccountList(this)
        val gson = GsonBuilder().create()
        val accountList = gson.fromJson(accountListString, Array<com.swidch.otacauth.Model.AccountItem>::class.java).toList()
        val list = ArrayList<com.swidch.otacauth.Model.AccountItem>()
        list.addAll(accountList)
        cmAlertDialog = CMAlertDialog(this, CMAlertDialog.SIMPLE_NEGATIVE_POSITIVE_ALERT, getString(R.string.account_delete_message), getString(R.string.alert_delete_button), getString(R.string.alert_cancel_button),{
            for ( i in list) {
                if(userId == i.userId) {
                    list.remove(i)
                    val json = gson.toJson(list)
                    com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setAccountList(this, json)

                    switchAccountListFragment()
                    cmAlertDialog?.dismiss()
                    return@CMAlertDialog
                }
            }
        }) {
            cmAlertDialog?.dismiss()
        }
        actionButton.text=getString(R.string.setting_bar_account_delete)
        actionButton.setOnClickListener {
            cmAlertDialog?.show()
        }

        mFragment = SettingAccountNameChangeFragment(userId)
        mFragment?.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.container, mFragment!!).commit()
    }

    companion object {
        private val TAG = AccountSettingActivity::class.java.simpleName
        private var mFragment: Fragment? = null
        const val ACCOUNT_LIST = "ACCOUNT_LIST"
        const val NAME = "NAME"
    }
}