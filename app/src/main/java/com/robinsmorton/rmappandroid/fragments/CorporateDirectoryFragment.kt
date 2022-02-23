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
import com.robinsmorton.rmappandroid.adapter.CorporateDirectoryAdapter
import com.robinsmorton.rmappandroid.databinding.FragmentCorporateDirectoryBinding
import com.robinsmorton.rmappandroid.model.CorporateUser
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel.Companion.cmd_hide_loading_sign_on_search_bar
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel.Companion.cmd_show_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel.Companion.cmd_show_loading_sign_on_search_bar
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
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
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
        binding.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(filterDataFromList(query))
        }
    }

    private fun init() {
        binding.imageViewClearSearchImage.setOnClickListener {
            binding.editTextSearch.setText("")
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
            binding.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.imageViewClearSearchImage.visibility = View.GONE
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
        binding.textViewListIsLoading.visibility = View.VISIBLE
        binding.loadingSpinnerSearchBar.visibility = View.VISIBLE
        binding.editTextSearch.isEnabled = false
    }

    private fun hideLoadingSignOnSearchBar() {
        binding.textViewListIsLoading.visibility = View.GONE
        binding.loadingSpinnerSearchBar.visibility = View.GONE
        binding.editTextSearch.isEnabled = true
        binding.editTextSearch.hint = getString(R.string.search_name)
    }
}