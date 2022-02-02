package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.CorporateDirectoryResponse
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