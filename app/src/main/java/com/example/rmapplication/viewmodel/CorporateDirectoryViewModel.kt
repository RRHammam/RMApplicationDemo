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
import com.example.rmapplication.repository.CorporateDirectoryRepository
import com.example.rmapplication.util.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CorporateDirectoryViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "CorporateDirectoryViewModel"

    private val corporateDirectoryRepository = CorporateDirectoryRepository(app)
    var corporateUsersListLiveData = MutableLiveData<List<CorporateUser>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()

    fun getCorporateDirectoryList(){
        if (corporateUsersListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        corporateDirectoryRepository.getCorporateDirectoryList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ corporateDirectoryResponse ->
                isLoading.set(false)
                corporateUsersListLiveData.value = corporateDirectoryResponse.value
            }, {
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Corporate directory list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}
