package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.estimatenumber.Item
import com.example.rmapplication.model.jobrequest.JobRequestValue
import com.example.rmapplication.repository.EstimateNumberRepository
import com.example.rmapplication.util.SessionManager
import com.example.rmapplication.util.SingleLiveEvent
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
                mainEstimateNumberList.addAll(estimateNumberResponse.items)
                estimateNumberListLiveData.value = estimateNumberResponse.items
            }, {
                hideLoading()
                Toast.makeText(getApplication(), "Error getting Estimate numbers list", Toast.LENGTH_SHORT).show()
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
