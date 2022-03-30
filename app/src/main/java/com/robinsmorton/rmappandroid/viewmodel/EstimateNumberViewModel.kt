package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.Value
import com.robinsmorton.rmappandroid.repository.EstimateNumberRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EstimateNumberViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "CorporateDirectoryViewModel"

    private val estimateNumberRepository = EstimateNumberRepository(app)
    var estimateNumberListLiveData = MutableLiveData<MutableList<Value>>()
    private val compositeDisposable = CompositeDisposable()
    var mainEstimateNumberList = mutableListOf<Value>()
    var eventCommand = SingleLiveEvent<Int>()

    fun getEstimateNumbersList(url: String = ""){
        if (url.isEmpty()) {
            showLoading()
        }
        estimateNumberRepository.getEstimateNumbersList(SessionManager.access_token, url).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ estimateNumberResponse ->
                estimateNumberListLiveData.value = estimateNumberResponse.value
                if (!estimateNumberResponse.nextLink.isNullOrEmpty()) {
                    getEstimateNumbersList(estimateNumberResponse.nextLink)
                } else {
                    Log.d(TAG, "Total job numbers - ${mainEstimateNumberList.lastIndex}")
                    hideLoadingOnSearchBar()
                }
                hideLoading()
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

    fun filterDataFromList(query: String): MutableList<Value>? {
        val filteredList = mutableListOf<Value>()
        return if (query.isNotEmpty()) {
            mainEstimateNumberList.forEach {
                if (isEstimateNumberMatching(it, query) || isTitleMatching(it, query) || isStatusMatching(it, query) || isDateMatching(it, query) ||
                        isLeadEstimatorMatching(it, query) || isOperationsManagerMatching(it, query)) {
                    filteredList.add(it)
                }
            }
            filteredList
        } else {
            mainEstimateNumberList
        }
    }

    private fun isTitleMatching(it: Value, query: String) = !it.fields.Title.isNullOrEmpty() && it.fields.Title.trim().lowercase().contains(query)

    private fun isEstimateNumberMatching(it: Value, query: String) =
        !it.fields.Estimate_x0020_Number.isNullOrEmpty() && it.fields.Estimate_x0020_Number.trim().lowercase().contains(query)

    private fun isStatusMatching(it: Value, query: String) = !it.fields.Status.isNullOrEmpty() && it.fields.Status.trim().lowercase().contains(query)

    private fun isDateMatching(it: Value, query: String) = !it.fields.Date.isNullOrEmpty() && it.fields.Date.trim().lowercase().contains(query)

    private fun isLeadEstimatorMatching(it: Value, query: String) = !it.fields.Lead_x0020_Estimator.isNullOrEmpty() && it.fields.Lead_x0020_Estimator.trim().lowercase().contains(query)

    private fun isOperationsManagerMatching(it: Value, query: String) = !it.fields.Operations_x0020_Manager.isNullOrEmpty() && it.fields.Operations_x0020_Manager.trim().lowercase().contains(query)

    fun clearMainEstimateNumberList() {
        mainEstimateNumberList.clear()
    }

    fun isMainEstimateNumberListEmpty(): Boolean {
        return mainEstimateNumberList.isEmpty()
    }

    fun addToMainList(list: MutableList<Value>) {
        mainEstimateNumberList.addAll(list)
    }

    companion object {
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
        const val cmd_hide_loading_sign_on_search_bar = 2
        const val cmd_show_loading_sign_on_search_bar = 3
    }

}
