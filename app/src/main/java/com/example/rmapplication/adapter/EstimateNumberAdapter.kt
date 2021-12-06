package com.example.rmapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rmapplication.R
import com.example.rmapplication.databinding.ItemEstimateNumberBinding
import com.example.rmapplication.model.estimatenumber.Item
import com.example.rmapplication.model.jobrequest.JobRequestValue

class EstimateNumberAdapter(val context: Context, private val estimateNumberList: MutableList<Item>) :
    RecyclerView.Adapter<EstimateNumberAdapter.EstimateNumberItemHolder>() {

    private val TAG = "EstimateNumberAdapter"
    private lateinit var binding: ItemEstimateNumberBinding


    inner class EstimateNumberItemHolder(val binding: ItemEstimateNumberBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindListItem(item: Item?) {
            val estimateNumberDetails = item?.fields
            ("Title: "+estimateNumberDetails?.Title).also { binding.textViewTitle.text = it }
            ("Estimate Number: "+estimateNumberDetails?.Estimate_x0020_Number).also { binding.textViewEstimateNumber.text = it }
            ("Status: "+estimateNumberDetails?.Status).also { binding.textViewStatus.text = it }
            ("Date: "+estimateNumberDetails?.Date).also { binding.textViewDate.text = it }
            ("Lead Estimator : "+estimateNumberDetails?.Lead_x0020_Estimator).also { binding.leadEstimator.text = it }
            ("Operations Manager : "+estimateNumberDetails?.Operations_x0020_Manager).also { binding.operationsManager.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstimateNumberItemHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.item_estimate_number,
            parent,
            false
        )
        return  EstimateNumberItemHolder(binding)
    }

    override fun onBindViewHolder(holder: EstimateNumberItemHolder, position: Int) {
        holder.bindListItem(estimateNumberList[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called - $estimateNumberList.size")
        return estimateNumberList.size
    }

    fun clearAndUpdateList(updatedList: MutableList<Item>?) {
        updatedList?.let {
            estimateNumberList.clear()
            estimateNumberList.addAll(it)
            notifyDataSetChanged()
        }
    }
}

