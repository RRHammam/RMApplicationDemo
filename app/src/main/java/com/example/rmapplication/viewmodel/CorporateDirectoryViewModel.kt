package com.example.rmapplication.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.core.util.PatternsCompat
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rmapplication.model.CorporateDirectoryResponse
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.repository.CorporateDirectoryRepository
import com.example.rmapplication.util.SessionManager
import com.example.rmapplication.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CorporateDirectoryViewModel(val app: Application) : AndroidViewModel(app) {

    val TAG = "CorporateDirectoryViewModel"

    private val corporateDirectoryRepository = CorporateDirectoryRepository(app)
    var corporateUsersListLiveData = MutableLiveData<MutableList<CorporateUser>>()
    var corporateUsersNextLinkListLiveData = MutableLiveData<MutableList<CorporateUser>>()
    var isLoading = ObservableBoolean(false)
    private val compositeDisposable = CompositeDisposable()
    var eventCommand = SingleLiveEvent<Int>()

    fun getCorporateDirectoryList() {
        showLoading()
        isLoading.set(true)
        corporateDirectoryRepository.getCorporateDirectoryList(SessionManager.access_token)
            .observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe({ corporateDirectoryResponse ->
                corporateUsersListLiveData.value =
                    filterCorporateUsersList(corporateDirectoryResponse)
                if (!corporateDirectoryResponse.nextUsersUrl.isNullOrEmpty()) {
                    getCorporateDirectoryListUsingNextLink(corporateDirectoryResponse.nextUsersUrl)
                } else {
                    hideLoading()
                    isLoading.set(false)
                }
            }, {
                hideLoading()
                isLoading.set(false)
                Toast.makeText(getApplication(), "Error getting Corporate directory list", Toast.LENGTH_SHORT).show()
                it.message?.let { it1 -> Log.e(TAG, it1) }
            })?.let { compositeDisposable.add(it) }
    }

    fun getCorporateDirectoryListUsingNextLink(nextLink: String) {
        if (!nextLink.isNullOrEmpty()) {
            corporateDirectoryRepository.getCorporateDirectoryListUsingNextLink(SessionManager.access_token, nextLink)
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe({ corporateDirectoryResponse ->
                    corporateUsersNextLinkListLiveData.value = filterCorporateUsersList(corporateDirectoryResponse)
                    if (!corporateDirectoryResponse.nextUsersUrl.isNullOrEmpty()) {
                        getCorporateDirectoryListUsingNextLink(corporateDirectoryResponse.nextUsersUrl)
                    } else {
                        hideLoading()
                        isLoading.set(false)
                        corporateUsersNextLinkListLiveData.value = mutableListOf()
                    }
                }, {
                    hideLoading()
                    isLoading.set(false)
                    Toast.makeText(getApplication(), "Error getting next link Corporate directory list", Toast.LENGTH_SHORT).show()
                    it.message?.let { it1 -> Log.e(TAG, it1) }
                })?.let { compositeDisposable.add(it) }
        } else {
            Toast.makeText(getApplication(), "End of the list.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun filterCorporateUsersList(corporateDirectoryResponse: CorporateDirectoryResponse): MutableList<CorporateUser> {
        return corporateDirectoryResponse.value.filter { corporateUser ->
            (!PatternsCompat.EMAIL_ADDRESS.matcher(corporateUser.displayName)
                .matches() && !corporateUser.jobTitle.isNullOrEmpty())
        }.toMutableList()
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
