package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.estimatenumber.EstimateNumbersResponse
import io.reactivex.Observable


class EstimateNumberRepository(val app: Application) {

    fun getEstimateNumbersList(accessToken: String?, nextLink: String): Observable<EstimateNumbersResponse> {
        Log.d("getEstimateNumberList", "getEstimateNumberList: accessToken $accessToken")
        return if(!nextLink.isNullOrEmpty()) {
            ApiServiceClient.getApiService().getEstimateNumberUsingNextLink(token = "Bearer $accessToken", nextLink)
        } else {
            ApiServiceClient.getApiService().getEstimateNumber(token = "Bearer $accessToken")
        }

        /*val estimateNumberResponse = JsonData.getEstimateNumbers(app.applicationContext)
        return Observable.create(ObservableOnSubscribe<EstimateNumberResponse> { emitter ->
            estimateNumberResponse?.let { emitter.onNext(it) }
            emitter.onComplete()
        })*/
    }
}