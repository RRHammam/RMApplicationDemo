package com.robinsmorton.rmappandroid.authentication

import com.microsoft.graph.models.User
import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request
import java.util.concurrent.CompletableFuture

class GraphHelper private constructor(private val authProvider: AuthenticationHelper){

    var mClient: GraphServiceClient<Request>? = null

    init {
        mClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient()
    }

    companion object {
        @Volatile private var INSTANCE: GraphHelper? = null

        fun getInstance(authProvider: AuthenticationHelper): GraphHelper {
            if(INSTANCE == null){
                INSTANCE = GraphHelper(authProvider)
            }

            return INSTANCE as GraphHelper
        }
    }


    fun getUser(): CompletableFuture<User>? {
        return this.mClient?.me()?.buildRequest()
            ?.select("displayName,mail,mailboxSettings,userPrincipalName,photo")
            ?.async
    }
}