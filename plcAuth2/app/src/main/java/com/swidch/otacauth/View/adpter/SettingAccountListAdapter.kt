package com.swidch.otacauth.View.adpter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.swidch.otacauth.View.setting.AccountSettingActivity
import com.swidch.otacauth.databinding.SettingListItemBinding
import java.util.Collections


class SettingAccountListAdapter: RecyclerView.Adapter<SettingAccountListAdapter.SettingAccountListViewHolder>() {
    var datalist = ArrayList<com.swidch.otacauth.Model.AccountItem>()
    var settingListRecyclerView: RecyclerView? = null
    var context: Context? = null
    private var itemDragHelper: ItemTouchHelper? = null

    private val helperCallback = DragHelperCallback { fromPos, toPos ->
        Collections.swap(datalist, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
        Log.d("시작: $fromPos", "변경: $toPos")
        Log.d("items", "$datalist")
        val gson = GsonBuilder().create()
        val json = gson.toJson(datalist)
        com.swidch.otacauth.Utils.sharedPreference.SharedPreferenceManager.setChangeAccountList(context!!, json)
        true
    }

    inner class SettingAccountListViewHolder(private val binding: SettingListItemBinding): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ClickableViewAccessibility")
        fun bind(listData: com.swidch.otacauth.Model.AccountItem, holder:SettingAccountListViewHolder) {
            val activity = context as AccountSettingActivity
            binding.changeAccountNameButton.setOnClickListener {
                activity.switchAccountNameChangeFragment(listData.userId!!)
            }
            binding.accountNameText.text = listData.accountName
            binding.run {
                pushButton.setOnTouchListener{ _, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        itemDragHelper?.startDrag(holder)
                    }
                    return@setOnTouchListener false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SettingAccountListViewHolder {
        val binding = SettingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context

        return SettingAccountListViewHolder(binding)
    }

    override fun getItemCount(): Int = datalist.size

    override fun onBindViewHolder(holder: SettingAccountListViewHolder, position: Int) {
        holder.bind(datalist[position], holder)
    }

    fun setSettingAccountList(list:ArrayList<com.swidch.otacauth.Model.AccountItem>, recyclerView: RecyclerView ) {
        this.datalist = list
        this.settingListRecyclerView = recyclerView
        itemDragHelper = ItemTouchHelper(helperCallback).apply { attachToRecyclerView(settingListRecyclerView) }
        notifyDataSetChanged()
    }

    inner class DragHelperCallback(private val actionDrag: (Int, Int) -> Boolean) : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlag = ItemTouchHelper.DOWN or ItemTouchHelper.UP
            return  makeMovementFlags(dragFlag, 0)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return actionDrag.invoke(viewHolder.adapterPosition, target.adapterPosition)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    }
}