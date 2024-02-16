package com.swidch.otacauth.View.adpter

import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.ssenstone.swidchauthsdk.SwidchAuthSDK
import com.ssenstone.swidchauthsdk.constants.SwidchAuthSDKError
import com.swidch.otacauth.Model.AccountItem
import com.swidch.otacauth.databinding.OtpListItemBinding
import java.lang.Exception
import java.util.concurrent.TimeUnit

class AccountListAdapter: RecyclerView.Adapter<AccountListAdapter.AccountListViewHolder>(), Filterable {
    var datalist = ArrayList<com.swidch.otacauth.Model.AccountItem>()
    var accountFilterList = ArrayList<com.swidch.otacauth.Model.AccountItem>()
    private var mSwidchAuthSDK:SwidchAuthSDK? = null
    private var filter = false

    inner class AccountListViewHolder(private val binding: OtpListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(listData: com.swidch.otacauth.Model.AccountItem, filterable:Boolean) {
            binding.accountNameText.text = listData.accountName
            if (!filterable) {
                initTimer(listData)
                generateOTAC(listData)
            }
        }

        fun generateOTAC(listData: com.swidch.otacauth.Model.AccountItem){
            mSwidchAuthSDK?.generateOtacPLC(listData.userId!!, listData.systemId!!, null, listData.svrDeviceId!!, null, null) { i, s, s1, s2 ->
                try {
                    if (i == SwidchAuthSDKError.NO_ERROR) {
                        binding.otpText.text = s2.toString()
                        listData.countDownTimer?.start()
                    } else {
                        Log.e("AccountList", "ERRORCODE : $i\n")
                    }
                } catch (e: Exception) {
                    Log.e("TEST_LOG", e.message!!)
                }
            }
        }

        fun initTimer(listData: com.swidch.otacauth.Model.AccountItem) {
            var timer = 60000
            if (listData.otacPeriod != null) {
                timer = listData.otacPeriod!!
            }

            listData.countDownTimer = object: CountDownTimer(timer.toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                    binding.otpTimer.text = seconds.toString()
                }

                override fun onFinish() {
                    generateOTAC(listData)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountListViewHolder {
        val binding = OtpListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        mSwidchAuthSDK = SwidchAuthSDK.getInstance(parent.context)
        if (mSwidchAuthSDK != null) {
            Log.d("AccountList", "SwidchAuth Library Version: ${mSwidchAuthSDK?.sdkVersion}")
        }

        return AccountListViewHolder(binding)
    }

    fun setAccountList(list:ArrayList<com.swidch.otacauth.Model.AccountItem>) {
        this.datalist = list
        accountFilterList = datalist
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = accountFilterList.size

    override fun onBindViewHolder(holder: AccountListViewHolder, position: Int) {
        holder.bind(accountFilterList[position], filter)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    accountFilterList = datalist
                } else {
                    val resultList = ArrayList<com.swidch.otacauth.Model.AccountItem>()
                    for (row in datalist) {
                        if (row.accountName?.lowercase()?.contains(constraint.toString().lowercase()) == true) {
                            resultList.add(row)
                        }
                    }
                    accountFilterList = resultList
                }
                filter = true
                val filterResults = FilterResults()
                filterResults.values = accountFilterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                accountFilterList = results?.values as ArrayList<AccountItem>
                notifyDataSetChanged()
            }

        }
    }
}