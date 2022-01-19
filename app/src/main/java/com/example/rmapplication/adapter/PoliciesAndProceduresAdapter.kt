package com.example.rmapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rmapplication.R
import com.example.rmapplication.databinding.PoliciesAndProceduresItemBinding
import com.example.rmapplication.fragments.PoliciesAndProcedureEventListener
import com.example.rmapplication.model.policiesandprocedures.PoliciesAndProceduresItem

class PoliciesAndProceduresAdapter(
    val context: Context,
    private val policiesAndProceduresList: MutableList<PoliciesAndProceduresItem>,
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
            (policiesAndProceduresDetails?.LinkFilename).also { binding.textViewListItem.text = it }

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
        holder.bindListItem(policiesAndProceduresList[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called - $policiesAndProceduresList.size")
        return policiesAndProceduresList.size
    }

    fun clearAndUpdateList(updatedList: MutableList<PoliciesAndProceduresItem>?) {
        updatedList?.let {
            policiesAndProceduresList.clear()
            policiesAndProceduresList.addAll(it)
            notifyDataSetChanged()
        }
    }

    private fun isRootElement(parentReferenceId: String): Boolean {
        policiesAndProceduresList.forEach {
            if (it.fields.eTag == parentReferenceId) {
                return true
            }
        }
        return false
    }

}
