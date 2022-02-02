package com.robinsmorton.rmappandroid.authentication

import android.app.Application
import android.util.Log

object AuthenticationUtil {

    fun getAuthHelperObj(app: Application): AuthenticationHelper? {
        var authHelperObj: AuthenticationHelper? = null
        AuthenticationHelper.getInstance(app).thenAccept { authHelper ->
            authHelperObj = authHelper
        }.exceptionally { exception ->
            Log.e("AUTH", "Error creating auth helper", exception)
            null
        }
        return authHelperObj
    }
}