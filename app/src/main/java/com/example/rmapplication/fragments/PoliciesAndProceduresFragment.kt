package com.example.rmapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rmapplication.R
import com.example.rmapplication.adapter.PoliciesAndProceduresAdapter
import com.example.rmapplication.databinding.FragmentPoliciesProceduresBinding
import com.example.rmapplication.model.policiesandprocedures.PoliciesAndProceduresItem
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel
import com.example.rmapplication.viewmodel.PoliciesAndProceduresViewModel
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
        subscribeToPoliciesAndProceduresListLiveData()
        viewModel.getPoliciesAndProcedures()
    }

    private fun subscribeToPoliciesAndProceduresListLiveData() {
        viewModel.policiesAndProceduresListLiveData.observe(viewLifecycleOwner, {
            setAdapter(viewModel.getRootElementsList())
        })
    }

    private fun setAdapter(it: MutableList<PoliciesAndProceduresItem>) {
        adapter = this.context?.let { it1 -> PoliciesAndProceduresAdapter(it1, it, this) }
        binding.policiesAndProceduresList.layoutManager = LinearLayoutManager(this.activity)
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

    fun showProgressBar() {
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onPoliciesAndProcedureItemClickedEvent(policiesAndProceduresItem: PoliciesAndProceduresItem?) {
        val childElementsList = viewModel.getChildElementsList(policiesAndProceduresItem?.fields?.eTag)
        adapter?.clearAndUpdateList(childElementsList)
    }
}

interface PoliciesAndProcedureEventListener {
    fun onPoliciesAndProcedureItemClickedEvent(policiesAndProceduresItem: PoliciesAndProceduresItem?)
}