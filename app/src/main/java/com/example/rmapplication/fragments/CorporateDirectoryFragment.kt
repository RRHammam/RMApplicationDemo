package com.example.rmapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmapplication.R
import com.example.rmapplication.activities.MainActivity
import com.example.rmapplication.adapter.CorporateDirectoryAdapter
import com.example.rmapplication.adapter.LobbyAdapter
import com.example.rmapplication.databinding.FragmentCorporateDirectoryBinding
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel

class CorporateDirectoryFragment: BaseFragment() {
    private lateinit var binding:FragmentCorporateDirectoryBinding
    private lateinit var viewModel: CorporateDirectoryViewModel
    private var adapter: CorporateDirectoryAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_corporate_directory, container, false)
        viewModel = ViewModelProvider(this).get(CorporateDirectoryViewModel::class.java)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToCorporateDirectoryListLiveData()
        viewModel.getCorporateDirectoryList()
        (activity as MainActivity).geBottomNavView()?.visibility = View.VISIBLE
    }

    fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.corporateUsersListLiveData.observe(viewLifecycleOwner, {
            adapter = this.context?.let { it1 -> CorporateDirectoryAdapter(it1, it) }
            binding.recyclerViewCorporateUsers.layoutManager = LinearLayoutManager(this.activity)
            binding.recyclerViewCorporateUsers.adapter = adapter
        })
    }
}