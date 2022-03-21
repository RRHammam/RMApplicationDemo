package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.estimatenumber.Item
import com.robinsmorton.rmappandroid.repository.EstimateNumberRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EstimateNumberViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "CorporateDirectoryViewModel"

    private val estimateNumberRepository = EstimateNumberRepository(app)
    var estimateNumberListLiveData = MutableLiveData<MutableList<Item>>()
    private val compositeDisposable = CompositeDisposable()
    var mainEstimateNumberList = mutableListOf<Item>()
    var eventCommand = SingleLiveEvent<Int>()

    fun getEstimateNumbersList(){
        showLoading()
        estimateNumberRepository.getEstimateNumbersList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ estimateNumberResponse ->
                hideLoading()
                hideLoadingOnSearchBar()
                mainEstimateNumberList.addAll(estimateNumberResponse.items)
                estimateNumberListLiveData.value = estimateNumberResponse.items
            }, {
                hideLoading()
                hideLoadingOnSearchBar()
                Toast.makeText(getApplication(), "Error getting Estimate numbers list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }
            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
        eventCommand.value = cmd_show_loading_sign_on_search_bar
    }

    private fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }

    private fun hideLoadingOnSearchBar() {
        eventCommand.value = cmd_hide_loading_sign_on_search_bar
    }

    fun filterDataFromList(query: String): MutableList<Item>? {
        val filteredList = mutableListOf<Item>()
        return if (query.isNotEmpty()) {
            mainEstimateNumberList.forEach {
                if (isEstimateNumberMatching(it, query) || isTitleMatching(it, query)) {
                    filteredList.add(it)
                }
            }
            filteredList
        } else {
            mainEstimateNumberList
        }
    }

    private fun isTitleMatching(it: Item, query: String) = !it.fields.Title.isNullOrEmpty() && it.fields.Title.trim().lowercase().contains(query)

    private fun isEstimateNumberMatching(it: Item, query: String) =
        !it.fields.Estimate_x0020_Number.isNullOrEmpty() && it.fields.Estimate_x0020_Number.trim().lowercase().contains(query)

    companion object {
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
        const val cmd_hide_loading_sign_on_search_bar = 2
        const val cmd_show_loading_sign_on_search_bar = 3
    }

}
