package com.example.rmapplication.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmapplication.R
import com.example.rmapplication.adapter.JobRequestAdapter
import com.example.rmapplication.databinding.FragmentJobRequestBinding
import com.example.rmapplication.databinding.FragmentPoliciesProceduresBinding
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.model.jobrequest.JobRequestValue
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel
import com.example.rmapplication.viewmodel.JobRequestViewModel
import com.example.rmapplication.viewmodel.PoliciesAndProceduresViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class PoliciesAndProceduresFragment: BaseFragment() {
    val TAG = "PoliciesAndProceduresFragment"
    private lateinit var binding: FragmentPoliciesProceduresBinding
    private lateinit var viewModel: PoliciesAndProceduresViewModel
    private var adapter: JobRequestAdapter? = null
    private var selectedItemForSearchType = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_policies_procedures, container, false)
        viewModel = ViewModelProvider(this).get(PoliciesAndProceduresViewModel::class.java)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        //subscribeToCorporateDirectoryListLiveData()
        //init()
        viewModel.getPoliciesAndProcedures()
    }

    fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.jobRequestListLiveData.observe(viewLifecycleOwner, {
        })
    }

    /*private fun init() {
        binding.searchBarJobNumber.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarJobNumber.editTextSearch.setText("")
        }
    }*/


   /* private fun setAdapter(it: MutableList<JobRequestValue>) {
        adapter = this.context?.let { it1 -> JobRequestAdapter(it1, it) }
        binding.jobRequestList.layoutManager = LinearLayoutManager(this.activity)
        binding.jobRequestList.adapter = adapter
    }*/

    override fun onResume() {
        super.onResume()
    }

    fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                CorporateDirectoryViewModel.cmd_show_loading_sign -> showProgressBar()

                CorporateDirectoryViewModel.cmd_hide_loading_sign -> hideProgressBar()
            }
        })
    }

    fun showProgressBar() {
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }


}