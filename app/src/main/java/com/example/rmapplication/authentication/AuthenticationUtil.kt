package com.example.rmapplication.authentication

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment

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