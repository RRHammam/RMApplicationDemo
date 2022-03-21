package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.adapter.JobRequestAdapter
import com.robinsmorton.rmappandroid.databinding.FragmentJobRequestBinding
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestValue
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel
import com.robinsmorton.rmappandroid.viewmodel.JobRequestViewModel
import com.robinsmorton.rmappandroid.viewmodel.JobRequestViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.JobRequestViewModel.Companion.cmd_hide_loading_sign_on_search_bar
import com.robinsmorton.rmappandroid.viewmodel.JobRequestViewModel.Companion.cmd_show_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.JobRequestViewModel.Companion.cmd_show_loading_sign_on_search_bar
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class JobRequestFragment: BaseFragment() {
    val TAG = "JobRequestFragment"
    private lateinit var binding: FragmentJobRequestBinding
    private lateinit var viewModel: JobRequestViewModel
    private var adapter: JobRequestAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_request, container, false)
        viewModel = ViewModelProvider(this).get(JobRequestViewModel::class.java)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        subscribeToCorporateDirectoryListLiveData()
        init()
        getJobRequests()
    }

    private fun getJobRequests() {
        viewModel.clearMainJobRequestList()
        viewModel.getJobRequestList()
    }

    private fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.jobRequestListLiveData.observe(viewLifecycleOwner, {
            if(viewModel.isMainJobRequestListEmpty()) {
                setAdapter(it)
                setOnTextChangedForSearchBar()
            } else {
                adapter?.addDataInList(it)
            }
            viewModel.addToMainList(it)
        })
    }

    private fun init() {
        binding.searchBarJobNumber.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarJobNumber.editTextSearch.setText("")
        }

        binding.titleBar.imageViewBackButton.setOnClickListener {
            navigateUp()
        }
    }

    private fun setAdapter(it: MutableList<JobRequestValue>) {
        adapter = this.context?.let { it1 -> JobRequestAdapter(it1, it) }
        binding.jobRequestList.layoutManager = LinearLayoutManager(this.activity)
        binding.jobRequestList.adapter = adapter
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarJobNumber.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(viewModel.filterDataFromList(query))
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarJobNumber.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarJobNumber.imageViewClearSearchImage.visibility = View.GONE
        }
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()

                cmd_show_loading_sign_on_search_bar -> showLoadingSignOnSearchBar()

                cmd_hide_loading_sign_on_search_bar -> hideLoadingSignOnSearchBar()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoadingSignOnSearchBar() {
        binding.searchBarJobNumber.textViewListIsLoading.visibility = View.VISIBLE
        binding.searchBarJobNumber.loadingSpinnerSearchBar.visibility = View.VISIBLE
        binding.searchBarJobNumber.editTextSearch.isEnabled = false
    }

    private fun hideLoadingSignOnSearchBar() {
        binding.searchBarJobNumber.textViewListIsLoading.visibility = View.GONE
        binding.searchBarJobNumber.loadingSpinnerSearchBar.visibility = View.GONE
        binding.searchBarJobNumber.editTextSearch.isEnabled = true
        binding.searchBarJobNumber.editTextSearch.hint = getString(R.string.search_name)
    }
}