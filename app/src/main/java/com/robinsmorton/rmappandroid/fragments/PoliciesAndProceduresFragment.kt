package com.robinsmorton.rmappandroid.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.adapter.PoliciesAndProceduresAdapter
import com.robinsmorton.rmappandroid.databinding.FragmentPoliciesProceduresBinding
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresItem
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel
import com.robinsmorton.rmappandroid.viewmodel.PoliciesAndProceduresViewModel
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class PoliciesAndProceduresFragment: BaseFragment(), PoliciesAndProcedureEventListener {
    val TAG = "PoliciesAndProceduresFragment"
    private lateinit var binding: FragmentPoliciesProceduresBinding
    private lateinit var viewModel: PoliciesAndProceduresViewModel
    private var adapter: PoliciesAndProceduresAdapter? = null
    private var selectedItemForSearchType = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_policies_procedures, container, false)
        viewModel = ViewModelProvider(this).get(PoliciesAndProceduresViewModel::class.java)
        handleBackPress()
        setOnClickListener()
        return with(binding){
            root
        }
    }

    private fun setOnClickListener() {
        binding.searchBarPoliciesAndProcedures.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarPoliciesAndProcedures.editTextSearch.setText("")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        subscribeToPoliciesAndProceduresListLiveData()
        viewModel.getPoliciesAndProcedures()
    }

    private fun subscribeToPoliciesAndProceduresListLiveData() {
        viewModel.policiesAndProceduresListLiveData.observe(viewLifecycleOwner, {
            val rootElementsList = viewModel.getRootElementsList()
            setAdapter(rootElementsList)
            setOnTextChangedForSearchBar()
        })
    }

    private fun setAdapter(it: MutableList<PoliciesAndProceduresItem>) {
        adapter = this.context?.let { it1 -> PoliciesAndProceduresAdapter(it1, it, this) }
        binding.policiesAndProceduresList.layoutManager = object : LinearLayoutManager(this.activity) {
            override fun isAutoMeasureEnabled(): Boolean {
                return false
            }
        }
        binding.policiesAndProceduresList.adapter = adapter
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                CorporateDirectoryViewModel.cmd_show_loading_sign -> showProgressBar()

                CorporateDirectoryViewModel.cmd_hide_loading_sign -> hideProgressBar()
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

    override fun onPoliciesAndProcedureItemClickedEvent(policiesAndProceduresItem: PoliciesAndProceduresItem?) {
        if(!viewModel.isDocument(policiesAndProceduresItem)){
            val childElementsList = viewModel.getChildElementsList(policiesAndProceduresItem?.fields?.eTag)
            addToPageStack()
            adapter?.clearAndUpdateList(childElementsList)
        } else {
            policiesAndProceduresItem?.webUrl?.let { openBrowser(it) }
        }

    }

    private fun addToPageStack() {
        val mapOfListByPageNumberSize = viewModel.mapOfListByPageNumber.size
        val lastPageList = mutableListOf<PoliciesAndProceduresItem>()
        adapter?.policiesAndProceduresList?.let { lastPageList.addAll(it) }
        viewModel.mapOfListByPageNumber[mapOfListByPageNumberSize + 1] = lastPageList
    }

    private fun handleBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "***handleOnBackPressed called")
            if(viewModel.mapOfListByPageNumber.isNotEmpty()) {
                adapter?.clearAndUpdateList(viewModel.mapOfListByPageNumber[viewModel.mapOfListByPageNumber.size])
                removeFromPageStack()
            } else {
                this.isEnabled = false
                activity?.onBackPressed()
            }
        }
    }

    private fun removeFromPageStack() {
        viewModel.mapOfListByPageNumber.remove(viewModel.mapOfListByPageNumber.size)
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarPoliciesAndProcedures.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(viewModel.filterDataFromList(query))
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarPoliciesAndProcedures.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarPoliciesAndProcedures.imageViewClearSearchImage.visibility = View.GONE
        }
    }


}

interface PoliciesAndProcedureEventListener {
    fun onPoliciesAndProcedureItemClickedEvent(policiesAndProceduresItem: PoliciesAndProceduresItem?)
}