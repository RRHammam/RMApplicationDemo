package com.example.rmapplication.repository

import android.app.Application
import android.util.Log
import com.example.rmapplication.apiserviceprovider.GraphApiServiceClient
import com.example.rmapplication.model.RmAppListResponse
import com.example.rmapplication.model.SitesResponse
import io.reactivex.Observable


class LobbyRepository(val app: Application) {

    fun getActiveDirectoryList(accessToken: String?): Observable<SitesResponse> {
        Log.d("LobbyRepository", "getActiveDirectoryList: accessToken $accessToken")
        return GraphApiServiceClient.getGraphApiService()
            .getListOfDirectories(token = "Bearer $accessToken")
        /*val sitesResponse = JsonData.getFollowedSites(app.applicationContext)
        return Observable.create(ObservableOnSubscribe<SitesResponse> { emitter ->
            sitesResponse?.let { emitter.onNext(it) }
            emitter.onComplete()
        })*/
    }

    fun getRmAppList(accessToken: String?): Observable<RmAppListResponse>{
        Log.d("LobbyRepository", "getRmAppList: accessToken $accessToken")
        return GraphApiServiceClient.getGraphApiService().getRmAppList(token = "Bearer $accessToken")
    }


}