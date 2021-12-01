package com.example.rmapplication.repository

import android.app.Application
import android.util.Log
import com.example.rmapplication.apiserviceprovider.ApiServiceClient
import com.example.rmapplication.model.CorporateDirectoryResponse
import io.reactivex.Observable


class CorporateDirectoryRepository(val app: Application) {

    fun getCorporateDirectoryList(accessToken: String?): Observable<CorporateDirectoryResponse> {
        Log.d("CorporateDirectoryRepository", "getCorporateDirectoryList: accessToken $accessToken")
        return ApiServiceClient.getApiService()
            .getCorporateDirectory(token = "Bearer $accessToken")
    }

    fun getCorporateDirectoryListUsingNextLink(accessToken: String?, url: String): Observable<CorporateDirectoryResponse> {
        Log.d("CorporateDirectoryRepository", "getCorporateDirectoryListUsingNextLink: url $url")
        return ApiServiceClient.getApiService()
            .getCorporateDirectoryWithNextLink(token = "Bearer $accessToken", url)
    }
}