package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.JobRequestItemBinding
import com.robinsmorton.rmappandroid.model.CorporateUser
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestValue

class JobRequestAdapter(val context: Context, private val jobRequestList: MutableList<JobRequestValue>) :
    RecyclerView.Adapter<JobRequestAdapter.JobRequestItemHolder>() {

    private val TAG = "JobRequestAdapter"
    private lateinit var binding: JobRequestItemBinding


    inner class JobRequestItemHolder(val binding: JobRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindListItem(jobRequest: JobRequestValue?) {
            val jobRequestDetails = jobRequest?.fields
            ("Title: "+jobRequestDetails?.Title).also { binding.Title.text = it }
            ("Job Number: "+jobRequestDetails?.Job_x0020_Number).also { binding.jobRequest.text = it }
            ("Estimate Number: "+jobRequestDetails?.Estimate_x0020_Number).also { binding.estimateNo.text = it }
            ("Start Date: "+jobRequestDetails?.Start_x0020_Date).also { binding.startDate.text = it }
            ("Approval: "+jobRequestDetails?.Approve).also { binding.approve.text = it }
            ("Project Manager : "+jobRequestDetails?.Sr_x002e__x0020_Project_x0020_Ma).also { binding.projectManager.text = it }
            ("Super Intendent : "+jobRequestDetails?.Superintendent).also { binding.superIntendent.text = it }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobRequestItemHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.job_request_item,
            parent,
            false
        )
        return  JobRequestItemHolder(binding)
    }

    override fun onBindViewHolder(holder: JobRequestItemHolder, position: Int) {
        holder.bindListItem(jobRequestList[position])
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount() called - $jobRequestList.size")
        return jobRequestList.size
    }

    fun clearAndUpdateList(updatedList: MutableList<JobRequestValue>?) {
        updatedList?.let {
            jobRequestList.clear()
            jobRequestList.addAll(it)
            notifyDataSetChanged()
        }
    }

    fun addDataInList(nextList: MutableList<JobRequestValue>){
        jobRequestList.addAll(nextList)
        notifyDataSetChanged()
    }
}
