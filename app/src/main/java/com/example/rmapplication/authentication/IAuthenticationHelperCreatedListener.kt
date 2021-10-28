package com.example.rmapplication.authentication

import com.microsoft.identity.client.exception.MsalException

interface IAuthenticationHelperCreatedListener {

    fun onCreated(authHelper: AuthenticationHelper?)
    fun onError(exception: MsalException?)

}