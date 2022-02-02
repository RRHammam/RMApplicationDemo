package com.robinsmorton.rmappandroid.apiserviceprovider

import com.robinsmorton.rmappandroid.model.*
import com.robinsmorton.rmappandroid.model.estimatenumber.EstimateNumberResponse
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestResponse
import com.robinsmorton.rmappandroid.model.policiesandprocedures.PoliciesAndProceduresResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface ApiService {

    @GET("v1.0/me/followedSites")
    fun getListOfDirectories(@Header("Authorization") token: String): Observable<SitesResponse>

    @GET("v1.0/sites/8763719b-ae4a-4f3a-a469-f2812ec4934e/lists/9a35d471-ce05-4339-a9be-9344939297db?expand=columns,items(expand=fields)")
    fun getRmAppList(@Header("Authorization") token: String): Observable<RmAppListResponse>

    @GET("v1.0/users")
    fun getCorporateDirectory(@Header("Authorization") token: String): Observable<CorporateDirectoryResponse>

    @GET
    fun getCorporateDirectoryWithNextLink(@Header("Authorization") token: String, @Url url: String): Observable<CorporateDirectoryResponse>

    @GET("v1.0/sites/8763719b-ae4a-4f3a-a469-f2812ec4934e/sites/6c1aed69-ca48-4759-88ae-805162a32512/lists/1537664d-c92b-4441-b3dd-a0432c4a536c/items?expand=columns,items(expand=fields)")
    fun getJobRequest(@Header("Authorization") token: String): Observable<JobRequestResponse>

    @GET("v1.0/sites/8763719b-ae4a-4f3a-a469-f2812ec4934e/sites/6c1aed69-ca48-4759-88ae-805162a32512/lists/1537664d-c92b-4441-b3dd-a0432c4a536c/items?expand=columns,items(expand=fields)")
    fun getEstimateNumber(@Header("Authorization") token: String): Observable<EstimateNumberResponse>

    @GET("v1.0/sites/8763719b-ae4a-4f3a-a469-f2812ec4934e/sites/85e41298-eba9-4c30-a9be-a31a8ae402ed/lists/7b074de8-0544-4850-a2f4-66a52687177d?expand=Items/children")
    fun getPoliciesAndProcedures(@Header("Authorization") token: String): Observable<PoliciesAndProceduresResponse>

    @GET("v1.0/sites/root/lists/2ad98e52-cf1c-4240-ac4d-839d3d41728d/?expand=items")
    fun getAppList(@Header("Authorization") token: String): Observable<EstimateNumberResponse>
}