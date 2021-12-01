package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.estimatenumber.Item
import com.example.rmapplication.repository.EstimateNumberRepository
import com.example.rmapplication.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class EstimateNumberViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "CorporateDirectoryViewModel"

    private val estimateNumberRepository = EstimateNumberRepository(app)
    var estimateNumberListLiveData = MutableLiveData<List<Item>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()

    fun getEstimateNumbersList(){
        if (estimateNumberListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        estimateNumberRepository.getEstimateNumbersList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ estimateNumberResponse ->
                isLoading.set(false)
                estimateNumberListLiveData.value = estimateNumberResponse.items
            }, {
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Estimate numbers list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }
            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
