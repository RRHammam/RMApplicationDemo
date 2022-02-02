package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestResponse
import io.reactivex.Observable


class JobRequestRepository(val app: Application) {

    fun getJobRequestList(accessToken: String?): Observable<JobRequestResponse> {
        Log.d("JobRequestRepository", "getJobRequestList: accessToken $accessToken")
        return ApiServiceClient.getApiService()
            .getJobRequest(token = "Bearer $accessToken")

        /*val jobRequestsResponse = JsonData.getJobRequests(app.applicationContext)
       return Observable.create(ObservableOnSubscribe<JobRequestResponse> { emitter ->
           jobRequestsResponse?.let { emitter.onNext(it) }
           emitter.onComplete()
       })*/
    }
}