package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.CorporateDirectoryResponse
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.model.Item
import com.example.rmapplication.model.jobrequest.JobRequestValue
import com.example.rmapplication.repository.CorporateDirectoryRepository
import com.example.rmapplication.repository.JobRequestRepository
import com.example.rmapplication.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class JobRequestViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "JobRequestViewModel"

    private val jobRequestRepository = JobRequestRepository(app)
    var jobRequestListLiveData = MutableLiveData<List<JobRequestValue>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()

    fun getJobRequestList(){
        if (jobRequestListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        jobRequestRepository.getJobRequestList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ jobRequestResponse ->
                isLoading.set(false)
                jobRequestListLiveData.value = jobRequestResponse.value
            }, {
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Job Request list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
