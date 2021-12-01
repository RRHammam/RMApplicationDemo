package com.example.rmapplication.repository

import android.app.Application
import android.util.Log
import com.example.rmapplication.apiserviceprovider.ApiServiceClient
import com.example.rmapplication.model.RmAppListResponse
import com.example.rmapplication.model.SitesResponse
import io.reactivex.Observable


class LobbyRepository(val app: Application) {

    fun getActiveDirectoryList(accessToken: String?): Observable<SitesResponse> {
        Log.d("LobbyRepository", "getActiveDirectoryList: accessToken $accessToken")
        return ApiServiceClient.getApiService()
            .getListOfDirectories(token = "Bearer $accessToken")
        /*val sitesResponse = JsonData.getFollowedSites(app.applicationContext)
        return Observable.create(ObservableOnSubscribe<SitesResponse> { emitter ->
            sitesResponse?.let { emitter.onNext(it) }
            emitter.onComplete()
        })*/
    }

    fun getRmAppList(accessToken: String?): Observable<RmAppListResponse>{
        Log.d("LobbyRepository", "getRmAppList: accessToken $accessToken")
        return ApiServiceClient.getApiService().getRmAppList(token = "Bearer $accessToken")
    }


}