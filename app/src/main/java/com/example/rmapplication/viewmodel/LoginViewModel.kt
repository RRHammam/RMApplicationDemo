package com.example.rmapplication.viewmodel

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import com.example.rmapplication.constants.Constants
import com.example.rmapplication.repository.LoginRepository
import com.example.rmapplication.util.SessionManager
import com.example.rmapplication.util.SingleLiveEvent
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

    fun doInteractiveSignIn(activity: Activity){
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
        SessionManager.access_token = currentAccessToken
        Log.d(TAG, String.format("Authentication successful - Access token: %s", currentAccessToken))
        Log.d(TAG, String.format("Authentication successful - Token expires on: %s", authenticationResult.expiresOn))
        for(scope in authenticationResult.scope){
            Log.d(TAG, String.format("Authentication successful - Scope: %s", scope))
        }
        Log.d(TAG, String.format("Authentication successful - TenantId: %s", authenticationResult.tenantId))
        Log.d(TAG, String.format("Authentication successful - AccountId: %s", authenticationResult.account.id))
        Log.d(TAG, String.format("Authentication successful - Authority: %s", authenticationResult.account.authority))
        getUser()
        eventCommand.value = CMD_LOGIN_SUCCESS
    }

    private fun getUser() {
        isLoading.set(true)
        loginRepository.getUserFromGraphApi()
            ?.thenAccept { user ->
                isLoading.set(false)

                Log.e(TAG, "User fetched successful - "+Gson().toJson(user))
                currentUser = user
                Constants.userDetails = user
            }
            ?.exceptionally { exception ->
                isLoading.set(false)
                Log.e(TAG, "Error getting /me", exception)
                null
            }
    }

    private fun handleSignInFailure(exception: Throwable?) {
        isLoading.set(false)
        if (exception is MsalServiceException) {
            // Exception when communicating with the auth server, likely config issue
            Log.e(TAG, "Service error authenticating", exception)
        } else if (exception is MsalClientException) {
            // Exception inside MSAL, more info inside MsalError.java
            Log.e(TAG, "Client error authenticating", exception)
        } else {
            Log.e(TAG, "Unhandled exception authenticating", exception)
        }
        eventCommand.value = CMD_LOGIN_FAILURE
    }

    companion object{
        const val CMD_LOGIN_SUCCESS = 0
        const val CMD_LOGIN_FAILURE = 1
    }

}