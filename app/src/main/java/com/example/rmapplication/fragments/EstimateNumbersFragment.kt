package com.example.rmapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmapplication.R
import com.example.rmapplication.adapter.CorporateDirectoryAdapter
import com.example.rmapplication.adapter.EstimateNumberAdapter
import com.example.rmapplication.adapter.JobRequestAdapter
import com.example.rmapplication.databinding.EstimateNumberLayoutBinding
import com.example.rmapplication.databinding.FragmentCorporateDirectoryBinding
import com.example.rmapplication.databinding.FragmentJobRequestBinding
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel
import com.example.rmapplication.viewmodel.EstimateNumberViewModel
import com.example.rmapplication.viewmodel.JobRequestViewModel

class EstimateNumbersFragment: BaseFragment() {
    private lateinit var binding: EstimateNumberLayoutBinding
    private lateinit var viewModel: EstimateNumberViewModel
    private var adapter: EstimateNumberAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.estimate_number_layout, container, false)
        viewModel = ViewModelProvider(this).get(EstimateNumberViewModel::class.java)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToCorporateDirectoryListLiveData()
        viewModel.getEstimateNumbersList()
    }

    fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.estimateNumberListLiveData.observe(viewLifecycleOwner, {
            adapter = this.context?.let { it1 -> EstimateNumberAdapter(it1, it) }
            binding.recyclerViewEstimateNumberList.layoutManager = LinearLayoutManager(this.activity)
            binding.recyclerViewEstimateNumberList.adapter = adapter
        })
    }

}