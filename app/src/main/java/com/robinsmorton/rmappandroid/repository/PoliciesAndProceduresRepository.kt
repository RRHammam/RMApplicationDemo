package com.robinsmorton.rmappandroid.repository

import android.app.Application
import android.util.Log
import com.robinsmorton.rmappandroid.apiserviceprovider.ApiServiceClient
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresResponse
import io.reactivex.Observable


class PoliciesAndProceduresRepository(val app: Application) {
    fun getPoliciesAndProcedures(accessToken: String?): Observable<PoliciesAndProceduresResponse> {
        Log.d("PoliciesAndProceduresRepository", "getPoliciesAndProcedures: accessToken $accessToken")
        return ApiServiceClient.getApiService().getPoliciesAndProcedures(token = "Bearer $accessToken")
    }
}