package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class JobRequestFragment: BaseFragment() {
    val TAG = "JobRequestFragment"
    private lateinit var binding: FragmentJobRequestBinding
    private lateinit var viewModel: JobRequestViewModel
    private var adapter: JobRequestAdapter? = null
    private var selectedItemForSearchType = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        viewModel.getJobRequestList()
    }

    fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.jobRequestListLiveData.observe(viewLifecycleOwner, {
            setAdapter(it)
            setOnTextChangedForSearchBar()
        })
    }

    private fun init() {
        binding.searchBarJobNumber.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarJobNumber.editTextSearch.setText("")
        }
    }


    private fun setAdapter(it: MutableList<JobRequestValue>) {
        adapter = this.context?.let { it1 -> JobRequestAdapter(it1, it) }
        binding.jobRequestList.layoutManager = LinearLayoutManager(this.activity)
        binding.jobRequestList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        setDropDownAdapter()
    }

    fun setDropDownAdapter() {
        val arrayAdapter = this.activity?.applicationContext?.let {
            ArrayAdapter(it, R.layout.dropdown_item,  R.id.textView, resources.getStringArray(R.array.job_numbers))
        }
        binding.autoCompleteTextViewJobNumber.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewJobNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                selectedItemForSearchType = char.toString()
                binding.textInputLayoutJobNumber.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                binding.searchBarJobNumber.constraintLayoutParentSearchBarLayout.visibility = View.VISIBLE
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.searchBarJobNumber.constraintLayoutParentSearchBarLayout.visibility = View.GONE
    }

    private fun filterDataFromList(query: String): MutableList<JobRequestValue>? {
        val filteredList = mutableListOf<JobRequestValue>()
        val filterByList = resources.getStringArray(R.array.job_numbers)
        return if (query.isNotEmpty()) {
            viewModel.mainJobRequestList.forEach {
                if (selectedItemForSearchType.trim() == filterByList[0]) {
                    if (it.fields.Job_x0020_Number.trim().lowercase().contains(query)) {
                        filteredList.add(it)
                    }
                } else {
                    if (it.fields.Title.trim().lowercase().contains(query)) {
                        filteredList.add(it)
                    }
                }
            }
            filteredList
        } else {
            viewModel.mainJobRequestList
        }
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarJobNumber.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(filterDataFromList(query))
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarJobNumber.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarJobNumber.imageViewClearSearchImage.visibility = View.GONE
        }
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