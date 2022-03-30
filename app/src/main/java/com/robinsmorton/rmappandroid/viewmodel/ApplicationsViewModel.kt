package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.Value
import com.robinsmorton.rmappandroid.repository.ApplicationsRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ApplicationsViewModel (
    val app : Application
) : AndroidViewModel(app){

    val TAG = "ApplicationsViewModel"

    companion object{
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
    }

    private val applicationsRepository = ApplicationsRepository(app)
    var eventCommand = SingleLiveEvent<Int>()
    var applicationsListLiveData = MutableLiveData<MutableList<Value>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()
    private val mainApplicationsList = mutableListOf<Value>()

    fun getListOfApplications() {
        if (applicationsListLiveData.value.isNullOrEmpty()) {
           showLoading()
        }
        applicationsRepository.getApplicationsList(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ apllicationsListResponse ->
                hideLoading()
                apllicationsListResponse.value?.let { mainApplicationsList.addAll(it) }
                applicationsListLiveData.value = apllicationsListResponse.value
            }, {
                Toast.makeText(getApplication(), "Error getting site list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }

            })?.let { compositeDisposable.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
    }

    private fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }

    fun getCategoriesFromMainList(): MutableList<String> {
        val categoryList = mutableListOf<String>()
        mainApplicationsList.forEach{
            if(!categoryList.contains(it.fields.field_1)) {
                categoryList.add(it.fields.field_1)
            }
        }
        return categoryList
    }

    fun getApplicationListForSelectedCategory (selectedCategory: String): MutableList<Value> {
        val appList = mutableListOf<Value>()
        mainApplicationsList.forEach {
            if (it.fields.field_1 == selectedCategory)
                appList.add(it)
        }
        return appList
    }

}