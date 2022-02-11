package com.robinsmorton.rmappandroid.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.repository.LoginRepository
import com.robinsmorton.rmappandroid.util.SessionManager
import com.robinsmorton.rmappandroid.util.SingleLiveEvent
import com.google.gson.Gson
import com.microsoft.graph.models.User
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.exception.MsalClientException
import com.microsoft.identity.client.exception.MsalServiceException
import com.microsoft.identity.client.exception.MsalUiRequiredException

class LoginViewModel (
    val app : Application
) : AndroidViewModel(app){

    private val TAG = "LoginViewModel"
    private val loginRepository = LoginRepository(app)
    var eventCommand = SingleLiveEvent<Int>()
    private var currentUser: User? = null
    private var currentAccessToken: String? = null
    var isLoading= ObservableBoolean(false)


    fun doSilentSignIn(shouldAttemptInteractive: Boolean, activity: Activity) {
        isLoading.set(true)
        loginRepository.doSilentSignIn()
            .thenAccept { authenticationResult ->
                run {
                    Log.d(TAG,"Silent login successful ")
                    handleSignInSuccess(authenticationResult)
                }
            }
            .exceptionally { exception ->
                // Check the type of exception and handle appropriately
                val cause: Throwable? = exception.cause
                if (cause is MsalUiRequiredException) {
                    Log.d(TAG, "Interactive login required")
                    if (shouldAttemptInteractive) {
                        doInteractiveSignIn(activity)
                    }
                } else if (cause is MsalClientException) {
                    val clientException =
                        cause
                    if (clientException.errorCode === "no_current_account" ||
                        clientException.errorCode === "no_account_found"
                    ) {
                        Log.d(TAG, "No current account, interactive login required")
                        if (shouldAttemptInteractive) {
                            doInteractiveSignIn(activity)
                        }
                    }
                } else {
                    Log.d(TAG,"Silent login failed ")
                    handleSignInFailure(cause)
                }
                null
            }
    }

    private fun doInteractiveSignIn(activity: Activity){
        isLoading.set(true)
        loginRepository.doInteractiveSignIn(activity)
            .thenAccept { authenticationResult ->
                handleSignInSuccess(authenticationResult)
            }
            .exceptionally { exception ->
                handleSignInFailure(exception)
                null
            }
    }

    // Handles the authentication result
    private fun handleSignInSuccess(authenticationResult: IAuthenticationResult) {
        isLoading.set(false)
        currentAccessToken = authenticationResult.accessToken
        Log.d(TAG, "*** token - $currentAccessToken")
        SessionManager.access_token = currentAccessToken
        //eventCommand.value = CMD_LOGIN_SUCCESS
        getUser()
    }

    private fun getUser() {
        isLoading.set(true)
        loginRepository.getUserFromGraphApi()
            ?.thenAccept { user ->
                isLoading.set(false)
                Log.e(TAG, "***User data fetched successful - "+Gson().toJson(user))
                currentUser = user
                Constants.userDetails = user
                eventCommand.postValue(CMD_LOGIN_SUCCESS)
            }
            ?.exceptionally { exception ->
                isLoading.set(false)
                Log.e(TAG, "***Error getting user data", exception)
                eventCommand.postValue(CMD_LOGIN_FAILURE)
                null
            }
    }

    private fun handleSignInFailure(exception: Throwable?) {
        isLoading.set(false)
        when (exception) {
            is MsalServiceException -> {
                // Exception when communicating with the auth server, likely config issue
                Log.e(TAG, "Service error authenticating", exception)
            }
            is MsalClientException -> {
                // Exception inside MSAL, more info inside MsalError.java
                Log.e(TAG, "Client error authenticating", exception)
            }
            else -> {
                Log.e(TAG, "Unhandled exception authenticating", exception)
                exception?.printStackTrace()
            }
        }
        eventCommand.value = CMD_LOGIN_FAILURE
    }

    companion object{
        const val CMD_LOGIN_SUCCESS = 0
        const val CMD_LOGIN_FAILURE = 1
    }

}