package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.PoliciesAndProceduresItemBinding
import com.robinsmorton.rmappandroid.fragments.PoliciesAndProcedureEventListener
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresItem

class PoliciesAndProceduresAdapter(
    val context: Context,
    val policiesAndProceduresList: MutableList<PoliciesAndProceduresItem>?,
    private val eventListener: PoliciesAndProcedureEventListener
) : RecyclerView.Adapter<PoliciesAndProceduresAdapter.PoliciesAndProceduresItemHolder>() {

    private val TAG = "PoliciesAndProceduresAdapter"
    private lateinit var binding: PoliciesAndProceduresItemBinding


    inner class PoliciesAndProceduresItemHolder(val binding: PoliciesAndProceduresItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var policiesAndProceduresItem: PoliciesAndProceduresItem? = null

        init{
            binding.root.setOnClickListener {
                eventListener.onPoliciesAndProcedureItemClickedEvent(policiesAndProceduresItem)
            }
        }

        fun bindListItem(policiesAndProcedures: PoliciesAndProceduresItem?) {
            policiesAndProceduresItem = policiesAndProcedures
            val policiesAndProceduresDetails = policiesAndProcedures?.fields
            (policiesAndProceduresDetails?.LinkFilename?.trim()).also { binding.textViewListItem.text = it }
            if(policiesAndProcedures?.fields?.ContentType == "Document"){
                binding.textViewListItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_doc_image, 0, 0,0)
            } else {
                binding.textViewListItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder_image,0, 0,0)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliciesAndProceduresItemHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.policies_and_procedures_item,
            parent,
            false
        )
        return  PoliciesAndProceduresItemHolder(binding)
    }

    override fun onBindViewHolder(holder: PoliciesAndProceduresItemHolder, position: Int) {
        holder.bindListItem(policiesAndProceduresList?.get(position))
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called - $policiesAndProceduresList.size")
        return policiesAndProceduresList?.size ?: 0
    }

    fun clearAndUpdateList(updatedList: MutableList<PoliciesAndProceduresItem>?) {
        updatedList?.let {
            policiesAndProceduresList?.clear()
            policiesAndProceduresList?.addAll(it)
            notifyDataSetChanged()
        }
    }
}
