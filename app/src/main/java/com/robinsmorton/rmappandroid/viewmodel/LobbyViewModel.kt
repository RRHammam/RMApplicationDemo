package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.*
import com.robinsmorton.rmappandroid.repository.LobbyRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LobbyViewModel (
    val app : Application
) : AndroidViewModel(app){

    val TAG = "LobbyViewModel"

    companion object{
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
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
        showLoading()
        lobbyRepository.getRmAppList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ rmAppListResponse ->
                rmAppListLiveData.value = orderList(rmAppListResponse.items)
                //rmAppListLiveData.value = reOrderList(rmAppListResponse.items)
                hideLoading()
            }, {
                hideLoading()
                Toast.makeText(getApplication(), "Error getting RM app list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun orderList(list: List<Item>): MutableList<Item> {
        val reOrderedList = mutableListOf<Item>()
        reOrderedList.addAll(list)
        list.forEach {
            if(it.fields?.AppOrder == 1) {
                reOrderedList.remove(it)
                reOrderedList.add(0, it)
            } else if(it.fields?.AppOrder == 2) {
                reOrderedList.remove(it)
                reOrderedList.add(1, it)
            } else if(it.fields?.AppOrder == 3) {
                reOrderedList.remove(it)
                reOrderedList.add(2, it)
            } else if(it.fields?.AppOrder == 4) {
                reOrderedList.remove(it)
                reOrderedList.add(3, it)
            } else if(it.fields?.AppOrder == 5) {
                reOrderedList.remove(it)
                reOrderedList.add(4, it)
            } else if(it.fields?.AppOrder == 6) {
                reOrderedList.remove(it)
                reOrderedList.add(5, it)
            } else if(it.fields?.AppOrder == 7) {
                reOrderedList.remove(it)
                reOrderedList.add(6, it)
            } else if(it.fields?.AppOrder == 8) {
                reOrderedList.remove(it)
                reOrderedList.add(10, it)
            } else if(it.fields?.AppOrder == 9) {
                reOrderedList.remove(it)
                reOrderedList.add(8, it)
            } else if(it.fields?.AppOrder == 10) {
                reOrderedList.remove(it)
                reOrderedList.add(9, it)
            } else if(it.fields?.AppOrder == 11) {
                reOrderedList.remove(it)
                reOrderedList.add(7, it)
            }
        }
        return reOrderedList
    }

    private fun iterateAndAddItemToThePosition(
        list: List<Item>,
        reOrderedList: MutableList<Item>,
        title: String,
        index: Int
    ) {
        list.forEach {
            if (it.fields?.Title?.trim() == title) {
                reOrderedList.add(index, it)
            }
        }
    }

    fun <T> mutableListWithCapacity(capacity: Int): MutableList<T> =
        ArrayList(capacity)

    fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
    }

    fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }

}