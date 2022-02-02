package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresItem
import com.robinsmorton.rmappandroid.repository.PoliciesAndProceduresRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PoliciesAndProceduresViewModel(val app: Application) : AndroidViewModel(app) {

    private val TAG = "PoliciesAndProceduresViewModel"

    private val policiesAndProceduresRepository = PoliciesAndProceduresRepository(app)
    var policiesAndProceduresListLiveData = MutableLiveData<MutableList<PoliciesAndProceduresItem>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()
    var eventCommand = SingleLiveEvent<Int>()
    var policiesAndProcedures = mutableListOf<PoliciesAndProceduresItem>()
    var previousPageList = mutableListOf<PoliciesAndProceduresItem>()
    var mapOfListByPageNumber = mutableMapOf<Int, MutableList<PoliciesAndProceduresItem>>()

    fun getPoliciesAndProcedures(){
        showLoading()
        if (policiesAndProceduresListLiveData.value.isNullOrEmpty()) {
            isLoading.set(true)
        }
        policiesAndProceduresRepository.getPoliciesAndProcedures(SessionManager.access_token).observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ policiesAndProceduresResponse ->
                hideLoading()
                isLoading.set(false)
                policiesAndProcedures.addAll(policiesAndProceduresResponse.items)
                updateEtag()
                policiesAndProceduresListLiveData.value = policiesAndProceduresResponse.items
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

    private fun showLoading(){
        eventCommand.value = cmd_show_loading_sign
    }

    private fun hideLoading(){
        eventCommand.value = cmd_hide_loading_sign
    }

    private fun updateEtag() {
        policiesAndProcedures.forEach {
            it.fields.eTag = it.fields.eTag.replace("\"", "")
            val stringEtag = it.fields.eTag.split(",")
            it.fields.eTag = stringEtag[0]
        }
    }

    private fun isRootElement(parentReferenceId: String): Boolean {
        var count = 0
        policiesAndProcedures.forEach {
            if (it.fields.eTag != parentReferenceId) {
                count++
            }
        }
        if(policiesAndProcedures.size == count){
            return true
        }
        return false
    }

    fun getRootElementsList(): MutableList<PoliciesAndProceduresItem> {
        val rootElementsList = mutableListOf<PoliciesAndProceduresItem>()
        policiesAndProcedures.forEach {
            if(isRootElement(it.parentReference.id)){
                rootElementsList.add(it)
            }
        }
        return rootElementsList
    }

    fun getChildElementsList(parentEtag: String?): MutableList<PoliciesAndProceduresItem> {
        val childElementsList = mutableListOf<PoliciesAndProceduresItem>()
        policiesAndProcedures.forEach {
            if (parentEtag ==  it.parentReference.id) {
                childElementsList.add(it)
            }
        }
        return childElementsList
    }

    fun isDocument(policiesAndProceduresItem: PoliciesAndProceduresItem?): Boolean{
        if(policiesAndProceduresItem?.fields?.ContentType == "Document"){
            return true
        }
        return false
    }

    fun filterDataFromList(query: String): MutableList<PoliciesAndProceduresItem>? {
        val filteredList = mutableListOf<PoliciesAndProceduresItem>()
        return if (query.isNotEmpty()) {
            policiesAndProcedures.forEach {
                if (it.fields.LinkFilename.trim().lowercase().contains(query)) {
                    filteredList.add(it)
                }
            }
            filteredList
        } else {
            getRootElementsList()
        }
    }

    companion object {
        const val cmd_show_loading_sign = 0
        const val cmd_hide_loading_sign = 1
    }

}
