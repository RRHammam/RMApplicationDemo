package com.example.rmapplication.repository

import android.app.Application
import android.util.Log
import com.example.rmapplication.model.estimatenumber.EstimateNumberResponse
import com.example.rmapplication.util.JsonData
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