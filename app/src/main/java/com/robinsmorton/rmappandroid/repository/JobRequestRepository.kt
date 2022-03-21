package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestResponse
import com.robinsmorton.rmappandroid.util.JsonData
import io.reactivex.Observable


class JobRequestRepository(val app: Application) {

    fun getJobRequestList(accessToken: String?, nextLink: String): Observable<JobRequestResponse> {
        return if(!nextLink.isNullOrEmpty()) {
            ApiServiceClient.getApiService().getJobRequestUsingNextLink(token = "Bearer $accessToken", nextLink)
        } else {
            ApiServiceClient.getApiService().getJobRequest(token = "Bearer $accessToken")
        }
    }
}