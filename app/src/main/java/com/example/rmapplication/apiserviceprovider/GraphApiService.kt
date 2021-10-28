package com.example.rmapplication.apiserviceprovider

import com.example.rmapplication.model.CorporateDirectoryResponse
import com.example.rmapplication.model.RmAppListResponse
import com.example.rmapplication.model.SitesResponse
import com.example.rmapplication.model.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header

interface GraphApiService {

    @GET("v1.0/me/followedSites")
    fun getListOfDirectories(@Header("Authorization") token: String): Observable<SitesResponse>

    @GET("v1.0/sites/8763719b-ae4a-4f3a-a469-f2812ec4934e/lists/9a35d471-ce05-4339-a9be-9344939297db?expand=columns,items(expand=fields)")
    fun getRmAppList(@Header("Authorization") token: String): Observable<RmAppListResponse>

    @GET("v1.0/users")
    fun getCorporateDirectory(@Header("Authorization") token: String): Observable<CorporateDirectoryResponse>
}