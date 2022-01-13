package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.jobrequest.JobRequestValue
import com.example.rmapplication.model.policiesandprocedures.Item
import com.example.rmapplication.repository.PoliciesAndProceduresRepository
import com.example.rmapplication.util.SessionManager
import com.example.rmapplication.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PoliciesAndProceduresViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "PoliciesAndProceduresViewModel"

    private val policiesAndProceduresRepository = PoliciesAndProceduresRepository(app)
    var jobRequestListLiveData = MutableLiveData<MutableList<JobRequestValue>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()
    var eventCommand = SingleLiveEvent<Int>()
    lateinit var policiesAndProcedures: MutableList<Item>

    fun getPoliciesAndProcedures(){
        showLoading()
        if (jobRequestListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        policiesAndProceduresRepository.getPoliciesAndProcedures(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ policiesAndProceduresResponse ->
                hideLoading()
                isLoading.set(false)
                policiesAndProcedures = policiesAndProceduresResponse.items
            }, {
                hideLoading()
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Policies and procedures", Toast.LENGTH_SHORT).show()
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
