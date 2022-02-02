package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

    fun getJobRequestList(){
        showLoading()
        if (jobRequestListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        jobRequestRepository.getJobRequestList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ jobRequestResponse ->
                hideLoading()
                isLoading.set(false)
                mainJobRequestList.addAll(jobRequestResponse.value)
                jobRequestListLiveData.value = jobRequestResponse.value
            }, {
                hideLoading()
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Job Request list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
    }

    fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }


    companion object {
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
    }

}
