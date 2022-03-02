package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
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
import com.robinsmorton.rmappandroid.adapter.EstimateNumberAdapter
import com.robinsmorton.rmappandroid.databinding.EstimateNumberLayoutBinding
import com.robinsmorton.rmappandroid.model.estimatenumber.Item
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.EstimateNumberViewModel.Companion.cmd_show_loading_sign
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class EstimateNumbersFragment: BaseFragment() {
    private lateinit var binding: EstimateNumberLayoutBinding
    private lateinit var viewModel: EstimateNumberViewModel
    private var adapter: EstimateNumberAdapter? = null
    private var selectedItemForSearchType = ""
    private lateinit var listForSearchItem: Array<String>

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
        binding.searchBarEstimateNumber.imageViewClearSearchImage.setOnClickListener {
            binding.searchBarEstimateNumber.editTextSearch.setText("")
        }
        listForSearchItem = resources.getStringArray(R.array.estimate_numbers)

        binding.titleBar.imageViewBackButton.setOnClickListener {
            navigateUp()
        }
    }


    private fun subscribeToEstimateNumberListLiveData() {
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

    private fun setDropDownAdapter() {
        val arrayAdapter = this.activity?.applicationContext?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.estimate_numbers,
                android.R.layout.simple_spinner_item
            )
        }
        arrayAdapter?.setDropDownViewResource(R.layout.dropdown_item)
        binding.spinnerEstimateNumber.adapter = arrayAdapter
        binding.spinnerEstimateNumber.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedItemForSearchType = listForSearchItem[position]
                    if(position != 0 ){
                        binding.searchBarEstimateNumber.constraintLayoutParentSearchBarLayout.visibility = View.VISIBLE
                    } else {
                        binding.searchBarEstimateNumber.editTextSearch.setText("")
                        binding.searchBarEstimateNumber.constraintLayoutParentSearchBarLayout.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        binding.searchBarEstimateNumber.constraintLayoutParentSearchBarLayout.visibility = View.GONE
    }

    private fun filterDataFromList(query: String): MutableList<Item>? {
        val filteredList = mutableListOf<Item>()
        val filterByList = resources.getStringArray(R.array.estimate_numbers)
        return if (query.isNotEmpty()) {
            if (selectedItemForSearchType.trim() == filterByList[0]) {
                filteredList.addAll(viewModel.mainEstimateNumberList)
            } else {
                viewModel.mainEstimateNumberList.forEach {
                    if (selectedItemForSearchType.trim() == filterByList[1]) {
                        if (it.fields.Estimate_x0020_Number.trim().lowercase().contains(query)) {
                            filteredList.add(it)
                        }
                    } else {
                        if (it.fields.Title.trim().lowercase().contains(query)) {
                            filteredList.add(it)
                        }
                    }
                }
            }
            filteredList
        } else {
            viewModel.mainEstimateNumberList
        }
    }

    private fun setOnTextChangedForSearchBar() {
        binding.searchBarEstimateNumber.editTextSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim().lowercase()
            toggleClearTextImageView(query)
            adapter?.clearAndUpdateList(filterDataFromList(query))
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
            when(it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()
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

}