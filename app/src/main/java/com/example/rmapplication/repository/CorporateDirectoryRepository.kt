package com.example.rmapplication.repository

import android.app.Application
import android.util.Log
import com.example.rmapplication.apiserviceprovider.GraphApiServiceClient
import com.example.rmapplication.model.CorporateDirectoryResponse
import com.example.rmapplication.model.RmAppListResponse
import com.example.rmapplication.model.SitesResponse
import io.reactivex.Observable


class CorporateDirectoryRepository(val app: Application) {

    fun getCorporateDirectoryList(accessToken: String?): Observable<CorporateDirectoryResponse> {
        Log.d("CorporateDirectoryRepository", "getCorporateDirectoryList: accessToken $accessToken")
        return GraphApiServiceClient.getGraphApiService()
            .getCorporateDirectory(token = "Bearer $accessToken")
    }
}