package com.example.rmapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmapplication.R
import com.example.rmapplication.activities.MainActivity
import com.example.rmapplication.adapter.CorporateDirectoryAdapter
import com.example.rmapplication.databinding.FragmentCorporateDirectoryBinding
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel.Companion.cmd_hide_loading_sign
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel.Companion.cmd_show_loading_sign
import kotlinx.android.synthetic.main.fragment_job_request.view.*
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class CorporateDirectoryFragment : BaseFragment() {
    val TAG = "CorporateDirectoryFragment"
    private lateinit var binding: FragmentCorporateDirectoryBinding
    private lateinit var viewModel: CorporateDirectoryViewModel
    private var adapter: CorporateDirectoryAdapter? = null
    private var corporateDirectoryMainList = mutableListOf<CorporateUser>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_corporate_directory,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(CorporateDirectoryViewModel::class.java)
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToCorporateDirectoryListLiveData()
        subscribeToCorporateDirectoryListNextLinkLiveData()
        subscribeToEventCommands()
        init()
        viewModel.getCorporateDirectoryList()
        (activity as MainActivity).geBottomNavView()?.visibility = View.VISIBLE
    }

    private fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.corporateUsersListLiveData.observe(viewLifecycleOwner, {
            setAdapter(it)
            corporateDirectoryMainList.addAll(it)
        })
    }

    private fun subscribeToCorporateDirectoryListNextLinkLiveData() {
        viewModel.corporateUsersNextLinkListLiveData.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                adapter?.addDataInList(it)
                corporateDirectoryMainList.addAll(it)
            } else {
                setOnTextChangedForSearchBar()
            }
        })
    }

    private fun setAdapter(it: MutableList<CorporateUser>) {
        adapter = this.requireContext().let { it1 -> CorporateDirectoryAdapter(it1, it) }
        binding.recyclerViewCorporateUsers.layoutManager =
            object : LinearLayoutManager(this.activity) {
                override fun isAutoMeasureEnabled(): Boolean {
                    return false
                }
            }
        binding.recyclerViewCorporateUsers.adapter = adapter
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarCorporateUserName.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(filterDataFromList(query))
        }
    }

    private fun init() {
        binding.searchBarCorporateUserName.editTextSearch.hint = getString(R.string.search_name)
        binding.searchBarCorporateUserName.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarCorporateUserName.editTextSearch.setText("")
        }
    }


    private fun filterDataFromList(query: String): MutableList<CorporateUser>? {
        val filteredList = mutableListOf<CorporateUser>()
        return if (query.isNotEmpty()) {
            corporateDirectoryMainList.forEach {
                if (it.displayName.trim().lowercase().contains(query)) {
                    filteredList.add(it)
                }
            }
            filteredList
        } else {
            corporateDirectoryMainList
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarCorporateUserName.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarCorporateUserName.imageViewClearSearchImage.visibility = View.GONE
        }
    }

    fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()
            }
        })
    }

    fun showProgressBar() {
        Log.d(TAG, "***showProgressBar()")
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        Log.d(TAG, "***hideProgressBar()")
        binding.progressBar.visibility = View.GONE
    }


}