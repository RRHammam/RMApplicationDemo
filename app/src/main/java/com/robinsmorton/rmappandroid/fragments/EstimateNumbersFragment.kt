package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.adapter.EstimateNumberAdapter
import com.robinsmorton.rmappandroid.databinding.EstimateNumberLayoutBinding
import com.robinsmorton.rmappandroid.model.Value
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_hide_loading_sign_on_search_bar
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_show_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_show_loading_sign_on_search_bar
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

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
        init()
        getEstimateNumbers()
    }

    private fun getEstimateNumbers() {
        viewModel.clearMainEstimateNumberList()
        viewModel.getEstimateNumbersList()
    }

    private fun init() {
        handleActivityViews()
        subscribeToEventCommands()
        subscribeToEstimateNumberListLiveData()
        binding.searchBarEstimateNumber.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarEstimateNumber.editTextSearch.setText("")
        }
        binding.titleBar.imageViewBackButton.setOnClickListener {
            navigateUp()
        }
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(false)
        }
    }

    private fun subscribeToEstimateNumberListLiveData() {
        viewModel.estimateNumberListLiveData.observe(viewLifecycleOwner, {
            if (viewModel.isMainEstimateNumberListEmpty()) {
                setAdapter(it)
                setOnTextChangedForSearchBar()
            } else {
                adapter?.addDataInList(it)
            }
            viewModel.addToMainList(it)
        })
    }

    private fun setAdapter(it: MutableList<Value>) {
        adapter = this.context?.let { it1 -> EstimateNumberAdapter(it1, it) }
        binding.recyclerViewEstimateNumberList.layoutManager = LinearLayoutManager(this.activity)
        binding.recyclerViewEstimateNumberList.adapter = adapter
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarEstimateNumber.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(viewModel.filterDataFromList(query))
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarEstimateNumber.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarEstimateNumber.imageViewClearSearchImage.visibility = View.GONE
        }
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when (it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()

                cmd_hide_loading_sign_on_search_bar -> hideLoadingSignOnSearchBar()

                cmd_show_loading_sign_on_search_bar -> showLoadingSignOnSearchBar()
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
        binding.searchBarEstimateNumber.textViewListIsLoading.visibility = View.VISIBLE
        binding.searchBarEstimateNumber.loadingSpinnerSearchBar.visibility = View.VISIBLE
        binding.searchBarEstimateNumber.editTextSearch.isEnabled = false
    }

    private fun hideLoadingSignOnSearchBar() {
        binding.searchBarEstimateNumber.textViewListIsLoading.visibility = View.GONE
        binding.searchBarEstimateNumber.loadingSpinnerSearchBar.visibility = View.GONE
        binding.searchBarEstimateNumber.editTextSearch.isEnabled = true
        binding.searchBarEstimateNumber.editTextSearch.hint = getString(R.string.search_name)
    }

}