package com.example.rmapplication.fragments

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
import com.example.rmapplication.R
import com.example.rmapplication.adapter.JobRequestAdapter
import com.example.rmapplication.databinding.FragmentJobRequestBinding
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.model.jobrequest.JobRequestValue
import com.example.rmapplication.viewmodel.JobRequestViewModel
import com.google.android.material.textfield.TextInputLayout

class JobRequestFragment: BaseFragment() {
    val TAG = "JobRequestFragment"
    private lateinit var binding: FragmentJobRequestBinding
    private lateinit var viewModel: JobRequestViewModel
    private var adapter: JobRequestAdapter? = null
    private var selectedItemForSearchType = ""
    private var jobRequestList = mutableListOf<JobRequestValue>()

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
        subscribeToCorporateDirectoryListLiveData()
        init()
        viewModel.getJobRequestList()
    }

    fun subscribeToCorporateDirectoryListLiveData() {
        viewModel.jobRequestListLiveData.observe(viewLifecycleOwner, {
            jobRequestList.addAll(it)
            setAdapter(jobRequestList)
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
            ArrayAdapter(
                it,
                R.layout.dropdown_item,
                resources.getStringArray(R.array.job_numbers)
            )
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
            jobRequestList.forEach {
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
            jobRequestList
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


}