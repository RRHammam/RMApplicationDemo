package com.robinsmorton.rmappandroid.viewmodel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import com.robinsmorton.rmappandroid.repository.LoginRepository
import com.robinsmorton.rmappandroid.util.SingleLiveEvent

class UserSettingsViewModel (
    val app : Application
) : AndroidViewModel(app){

    private val TAG = "UserSettingsViewModel"
    private val loginRepository = LoginRepository(app)
    var eventCommand = SingleLiveEvent<Int>()
    var isLoading= ObservableBoolean(false)


    fun doLogout() {
        isLoading.set(true)
        loginRepository.doSignOut(object : ISingleAccountPublicClientApplication.SignOutCallback {
            override fun onSignOut() {
                isLoading.set(false)
                Log.d("AUTHHELPER", "Signed out")
                eventCommand.value = CMD_LOGOUT_SUCCESS
            }

            override fun onError(exception: MsalException) {
                isLoading.set(false)
                Log.d("AUTHHELPER", "MSAL error signing out", exception)
                eventCommand.value = CMD_LOGOUT_FAILURE
            }
        })
    }

    companion object{
        const val CMD_LOGOUT_SUCCESS = 0
        const val CMD_LOGOUT_FAILURE = 1
    }

}