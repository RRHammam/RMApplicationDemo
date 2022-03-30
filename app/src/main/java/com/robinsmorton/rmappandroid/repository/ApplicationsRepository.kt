package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.ApplicationsListResponse
import com.robinsmorton.rmappandroid.model.RmAppListResponse
import com.robinsmorton.rmappandroid.model.SitesResponse
import io.reactivex.Observable


class ApplicationsRepository(val app: Application) {

    fun getApplicationsList(accessToken: String?): Observable<ApplicationsListResponse> {
        Log.d("ApplicationsRepository", "getApplicationsList: accessToken $accessToken")
        return ApiServiceClient.getApiService()
            .getListOfApplications(token = "Bearer $accessToken")
    }

}