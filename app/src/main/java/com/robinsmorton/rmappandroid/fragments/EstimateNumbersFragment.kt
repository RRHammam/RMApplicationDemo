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
import com.robinsmorton.rmappandroid.adapter.EstimateNumberAdapter
import com.robinsmorton.rmappandroid.databinding.EstimateNumberLayoutBinding
import com.robinsmorton.rmappandroid.model.estimatenumber.Item
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class EstimateNumbersFragment: BaseFragment() {
    private lateinit var binding: EstimateNumberLayoutBinding
    private lateinit var viewModel: EstimateNumberViewModel
    private var adapter: EstimateNumberAdapter? = null
    private var selectedItemForSearchType = ""

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
        viewModel.getEstimateNumbersList()
    }

    private fun init() {
        subscribeToEventCommands()
        subscribeToEstimateNumberListLiveData()
        binding.searchBarEstimateNumberr.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarEstimateNumberr.editTextSearch.setText("")
        }
    }


    fun subscribeToEstimateNumberListLiveData() {
        viewModel.estimateNumberListLiveData.observe(viewLifecycleOwner, {
            adapter = this.context?.let { it1 -> EstimateNumberAdapter(it1, it) }
            binding.recyclerViewEstimateNumberList.layoutManager = LinearLayoutManager(this.activity)
            binding.recyclerViewEstimateNumberList.adapter = adapter
            setOnTextChangedForSearchBar()
        })
    }

    override fun onResume() {
        super.onResume()
        setDropDownAdapter()
    }

    fun setDropDownAdapter() {
        val arrayAdapter = this.activity?.applicationContext?.let {
            ArrayAdapter(it, R.layout.dropdown_item, R.id.textView, resources.getStringArray(R.array.estimate_numbers))
        }
        binding.autoCompleteTextViewEstimateNumber.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewEstimateNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                selectedItemForSearchType = char.toString()
                binding.textInputLayoutEstimateNumber.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                binding.searchBarEstimateNumberr.constraintLayoutParentSearchBarLayout.visibility = View.VISIBLE
                binding.autoCompleteTextViewEstimateNumber.clearFocus()
                binding.searchBarEstimateNumberr.constraintLayoutParentSearchBarLayout.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        binding.searchBarEstimateNumberr.constraintLayoutParentSearchBarLayout.visibility = View.GONE
    }

    private fun filterDataFromList(query: String): MutableList<Item>? {
        val filteredList = mutableListOf<Item>()
        val filterByList = resources.getStringArray(R.array.estimate_numbers)
        return if (query.isNotEmpty()) {
            viewModel.mainEstimateNumberList.forEach {
                if (selectedItemForSearchType.trim() == filterByList[0]) {
                    if (it.fields.Estimate_x0020_Number.trim().lowercase().contains(query)) {
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
            viewModel.mainEstimateNumberList
        }
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarEstimateNumberr.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(filterDataFromList(query))
        }
    }

    private fun toggleClearTextImageView(query: String) {
        if (query.isNotEmpty()) {
            binding.searchBarEstimateNumberr.imageViewClearSearchImage.visibility = View.VISIBLE
        } else {
            binding.searchBarEstimateNumberr.imageViewClearSearchImage.visibility = View.GONE
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