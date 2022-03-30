package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestResponse
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestValue
import com.robinsmorton.rmappandroid.repository.JobRequestRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JobRequestViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "JobRequestViewModel"
    private val jobRequestRepository = JobRequestRepository(app)
    var jobRequestListLiveData = MutableLiveData<MutableList<JobRequestValue>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()
    var mainJobRequestList = mutableListOf<JobRequestValue>()
    var eventCommand = SingleLiveEvent<Int>()

    fun getJobRequestList(url: String = ""){
        if (url.isEmpty()) {
            showLoading()
        }
        jobRequestRepository.getJobRequestList(SessionManager.access_token, url).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ jobRequestResponse ->
                jobRequestListLiveData.value = jobRequestResponse.value
                if (!jobRequestResponse.nextLink.isNullOrEmpty()) {
                   getJobRequestList(jobRequestResponse.nextLink)
                } else {
                    Log.d(TAG, "Total job numbers - ${mainJobRequestList.lastIndex}")
                    hideLoadingOnSearchBar()
                }
                hideLoading()
            }, {
                Log.d(TAG, "Total job numbers - ${mainJobRequestList.lastIndex}")
                hideLoadingOnSearchBar()
                Toast.makeText(getApplication(), "Error getting Job Request list", Toast.LENGTH_SHORT).show()
                it.localizedMessage?.let { it1 -> Log.e(TAG, it1) }
                it.printStackTrace()
            })?.let { compositeDisposable.add(it) }
    }

    fun addToMainList(list: MutableList<JobRequestValue>) {
        mainJobRequestList.addAll(list)
    }

    fun clearMainJobRequestList() {
        mainJobRequestList.clear()
    }

    fun isMainJobRequestListEmpty(): Boolean {
        return mainJobRequestList.isEmpty()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
        isLoading.set(true)
        eventCommand.value = cmd_show_loading_sign_on_search_bar
    }

    private fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }

    private fun hideLoadingOnSearchBar() {
        isLoading.set(false)
        eventCommand.value = cmd_hide_loading_sign_on_search_bar
    }

    fun filterDataFromList(query: String): MutableList<JobRequestValue>? {
        val filteredList = mutableListOf<JobRequestValue>()
        return if (query.isNotEmpty()) {
            mainJobRequestList.forEach {
                if (isMatchingJobNumber(it, query) || isMatchingTitle(it, query) || isMatchingEstimateNumber(it, query) || isMatchingApproval(it, query)
                    || isMatchingStartDate(it, query) || isMatchingSuperintendent(it, query) || isMatchingProjectManager(it, query)) {
                    filteredList.add(it)
                }
            }
            filteredList
        } else {
            mainJobRequestList
        }
    }

    private fun isMatchingTitle(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Title.isNullOrEmpty() && it.fields.Title.trim().lowercase().contains(query)

    private fun isMatchingJobNumber(it: JobRequestValue, query: String) =
        !it.fields.Job_x0020_Number.isNullOrEmpty() && it.fields.Job_x0020_Number.trim().lowercase()
            .contains(query)

    private fun isMatchingEstimateNumber(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Estimate_x0020_Number.isNullOrEmpty() && it.fields.Estimate_x0020_Number.trim().lowercase().contains(query)

    private fun isMatchingStartDate(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Start_x0020_Date.isNullOrEmpty() && it.fields.Start_x0020_Date.trim().lowercase().contains(query)

    private fun isMatchingSuperintendent(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Superintendent.isNullOrEmpty() && it.fields.Superintendent.trim().lowercase().contains(query)

    private fun isMatchingApproval(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Approve.isNullOrEmpty() && it.fields.Approve.trim().lowercase().contains(query)


    private fun isMatchingProjectManager(
        it: JobRequestValue,
        query: String
    ) = !it.fields.Sr_x002e__x0020_Project_x0020_Ma.isNullOrEmpty() && it.fields.Sr_x002e__x0020_Project_x0020_Ma.trim().lowercase().contains(query)

    companion object {
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
        const val cmd_hide_loading_sign_on_search_bar = 2
        const val cmd_show_loading_sign_on_search_bar = 3
    }

}
