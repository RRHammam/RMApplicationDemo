package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.model.estimatenumber.EstimateNumberResponse
import com.robinsmorton.rmappandroid.util.JsonData
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe


class EstimateNumberRepository(val app: Application) {

    fun getEstimateNumbersList(accessToken: String?): Observable<EstimateNumberResponse> {
        Log.d("getEstimateNumberList", "getEstimateNumberList: accessToken $accessToken")
        /*return GraphApiServiceClient.getGraphApiService()
            .getEstimateNumber(token = "Bearer $accessToken")*/

        val estimateNumberResponse = JsonData.getEstimateNumbers(app.applicationContext)
        return Observable.create(ObservableOnSubscribe<EstimateNumberResponse> { emitter ->
            estimateNumberResponse?.let { emitter.onNext(it) }
            emitter.onComplete()
        })
    }
}