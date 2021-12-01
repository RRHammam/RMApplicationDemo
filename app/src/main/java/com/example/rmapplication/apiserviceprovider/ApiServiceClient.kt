package com.example.rmapplication.apiserviceprovider

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceClient {

    private  val  baseUrl = "https://graph.microsoft.com/"

    private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private  val client = OkHttpClient.Builder().addInterceptor(logging).build()

    private  val apiService = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl(baseUrl)
        .client(client)
        .build()
        .create(ApiService::class.java)

    fun getApiService(): ApiService {
        return apiService
    }
}

class OAuthInterceptor(private val tokenType: String, private val accessToken: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$accessToken").build()
        return chain.proceed(request)
    }
}