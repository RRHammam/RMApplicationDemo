package com.robinsmorton.rmappandroid.repository

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import com.robinsmorton.rmappandroid.authentication.AuthenticationHelper
import com.robinsmorton.rmappandroid.authentication.GraphHelper
import com.microsoft.graph.models.User
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import java.util.concurrent.CompletableFuture


class LoginRepository constructor(private val app: Application) {

    private val TAG = "LoginRepository"
    private lateinit var mAuthHelper: AuthenticationHelper
    var isAuthenticationSuccessful = ObservableBoolean(false)
    var currentAccessToken: String? = null
    var currentUser: User? = null

    init {
        AuthenticationHelper.getInstance(app).thenAccept { authHelper ->
            mAuthHelper = authHelper
        }.exceptionally { exception ->
            Log.e(TAG, "Error creating auth helper", exception)
            null
        }
    }

    // Silently sign in - used if there is already a
    // user account in the MSAL cache
    fun doSilentSignIn(): CompletableFuture<IAuthenticationResult>{
        return mAuthHelper.acquireTokenSilently()
    }

    // Prompt the user to sign in
    fun doInteractiveSignIn(activity: Activity): CompletableFuture<IAuthenticationResult> {
        return mAuthHelper.acquireTokenInteractively(activity)
    }

    fun getUserFromGraphApi(): CompletableFuture<User>? {
        val graphHelper: GraphHelper = GraphHelper.getInstance(mAuthHelper)
        return graphHelper.getUser()
    }

    fun doSignOut(signOutCallback: ISingleAccountPublicClientApplication.SignOutCallback) {
        mAuthHelper.signOut(signOutCallback)
    }
}