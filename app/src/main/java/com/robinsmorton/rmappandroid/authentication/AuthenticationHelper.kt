package com.robinsmorton.rmappandroid.authentication

import android.app.Activity
import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.R
import com.microsoft.graph.authentication.BaseAuthenticationProvider
import com.microsoft.identity.client.*
import com.microsoft.identity.client.ISingleAccountPublicClientApplication.SignOutCallback
import com.microsoft.identity.client.exception.MsalException
import java.net.URL
import java.util.concurrent.CompletableFuture


class AuthenticationHelper private constructor(val app: Application, val listener: IAuthenticationHelperCreatedListener) :
    BaseAuthenticationProvider(){
    private val TAG = "AuthenticationHelper"
    private lateinit var mPCA: ISingleAccountPublicClientApplication
    private val mScopes = arrayOf("User.Read", "MailboxSettings.Read", "Calendars.ReadWrite")

    init {
        PublicClientApplication.createSingleAccountPublicClientApplication(app.applicationContext, R.raw.msal_config,
            object : IPublicClientApplication.ISingleAccountApplicationCreatedListener {
                override fun onCreated(application: ISingleAccountPublicClientApplication) {
                    mPCA = application
                    listener.onCreated(this@AuthenticationHelper)
                }

                override fun onError(exception: MsalException?) {
                    Log.e("AUTHHELPER", "Error creating MSAL application", exception)
                    listener.onError(exception)
                }
            })


    }

    companion object {

        @Volatile private var INSTANCE: AuthenticationHelper? = null


        fun getInstance(app: Application): CompletableFuture<AuthenticationHelper> {
            return if(INSTANCE == null){
                val future: CompletableFuture<AuthenticationHelper> = CompletableFuture()
                INSTANCE = AuthenticationHelper(app, object:
                    IAuthenticationHelperCreatedListener {
                    override fun onCreated(authHelper: AuthenticationHelper?) {
                        future.complete(authHelper)
                    }

                    override fun onError(exception: MsalException?) {
                        future.completeExceptionally(exception)
                    }
                })
                future
            } else {
                CompletableFuture.completedFuture(INSTANCE)
            }
        }

    }

    fun acquireTokenInteractively(activity: Activity): CompletableFuture<IAuthenticationResult> {
        val future: CompletableFuture<IAuthenticationResult> = CompletableFuture()
        mPCA.signIn(activity, null, mScopes, getAuthenticationCallback(future))
        return future
    }

    fun acquireTokenSilently(): CompletableFuture<IAuthenticationResult> {
        // Get the authority from MSAL config
        val authority = mPCA.configuration.defaultAuthority.authorityURL.toString()
        Log.d("AuthenticationHelper","authority - $authority")
        val future: CompletableFuture<IAuthenticationResult> = CompletableFuture()
        mPCA.acquireTokenSilentAsync(mScopes, authority, getAuthenticationCallback(future))
        return future
    }

    fun signOut(signOutCallback: SignOutCallback) {
        mPCA.signOut(signOutCallback)
    }

    private fun getAuthenticationCallback(future: CompletableFuture<IAuthenticationResult>): AuthenticationCallback {
        return object : AuthenticationCallback {
            override fun onCancel() {
                try {
                    future.cancel(true)
                } catch (e: Exception) {
                    Log.e(TAG, e.localizedMessage)
                }
            }

            override fun onSuccess(authenticationResult: IAuthenticationResult?) {
                try {
                    future.complete(authenticationResult)
                }  catch (e: Exception) {
                    Log.e(TAG, e.localizedMessage)
                }
            }

            override fun onError(exception: MsalException?) {
                try {
                    future.completeExceptionally(exception)
                }  catch (e: Exception) {
                    Log.e(TAG, e.localizedMessage)
                }
            }
        }
    }

    override fun getAuthorizationTokenAsync(requestUrl: URL): CompletableFuture<String> {
        return if (shouldAuthenticateRequestWithUrl(requestUrl)) {
            acquireTokenSilently().thenApply { result -> result.accessToken }
        } else CompletableFuture.completedFuture(null)
    }

}