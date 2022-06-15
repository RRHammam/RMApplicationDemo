package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.BottomsheetDialogListItemBinding
import com.robinsmorton.rmappandroid.databinding.PoliciesAndProceduresItemBinding
import com.robinsmorton.rmappandroid.fragments.PoliciesAndProcedureEventListener
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresItem

class BottomSheetListDialogAdapter(
    val context: Context,
    val items: LinkedHashMap<String, () -> Unit>,
   ) : RecyclerView.Adapter<BottomSheetListDialogAdapter.BottomSheetListDialogItemHolder>() {

    private val TAG = "BottomSheetListDialogAdapter"
    private lateinit var binding: BottomsheetDialogListItemBinding


    inner class BottomSheetListDialogItemHolder(val binding: BottomsheetDialogListItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var itemTitle: String? = null
        private var itemFunc: (() -> Unit)? = null

        init{
            binding.root.setOnClickListener(this)
        }

        fun bindListItem(itemTitle: String, itemFunc: (() -> Unit)) {
            this.itemTitle = itemTitle
            this.itemFunc = itemFunc
            binding.textViewListItem.text = itemTitle
            when (itemTitle) {
                context.resources.getString(R.string.add_to_contacts) -> {
                    binding.imageViewEnd.setBackgroundResource(R.drawable.ic_plus)
                }
                context.resources.getString(R.string.call) -> {
                    binding.imageViewEnd.setBackgroundResource(R.drawable.ic_call)
                }
                context.resources.getString(R.string.text) -> {
                    binding.imageViewEnd.setBackgroundResource(R.drawable.ic_message)
                }
            }
        }

        override fun onClick(v: View?) {
            itemFunc?.invoke()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetListDialogItemHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.bottomsheet_dialog_list_item,
            parent,
            false
        )
        return  BottomSheetListDialogItemHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetListDialogItemHolder, position: Int) {
        val itemTitle = items.keys.toTypedArray()[position]
        val itemFunc = items[itemTitle]
        itemFunc?.let { holder.bindListItem(itemTitle, it) }
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called - $items.size")
        return items?.size ?: 0
    }
}
