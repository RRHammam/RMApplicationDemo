package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.Item
import com.example.rmapplication.model.Site
import com.example.rmapplication.repository.LobbyRepository
import com.example.rmapplication.util.SessionManager
import com.example.rmapplication.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LobbyViewModel (
    val app : Application
) : AndroidViewModel(app){

    val TAG = "LobbyViewModel"

    companion object{
        const val CMD_SUCCESS = 0
        const val CMD_FAILURE = 1
    }

    private val lobbyRepository = LobbyRepository(app)
    var eventCommand = SingleLiveEvent<Int>()
    var directoryListLiveData = MutableLiveData<List<Site>>()
    var rmAppListLiveData = MutableLiveData<List<Item>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()

    fun getListOfDirectories() {
        if (directoryListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        lobbyRepository.getActiveDirectoryList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ siteResponse ->
                isLoading.set(false)
                directoryListLiveData.value = siteResponse.value
            }, {
                Toast.makeText(getApplication(), "Error getting site list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    fun getRmAppList() {
        if (rmAppListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        lobbyRepository.getRmAppList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ rmAppListResponse ->
                isLoading.set(false)
                rmAppListLiveData.value = rmAppListResponse.items
            }, {
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting RM app list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}