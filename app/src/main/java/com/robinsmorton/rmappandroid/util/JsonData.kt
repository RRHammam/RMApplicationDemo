package com.robinsmorton.rmappandroid.util

import android.content.Context
import com.robinsmorton.rmappandroid.model.estimatenumber.EstimateNumbersResponse
import com.robinsmorton.rmappandroid.model.SitesResponse
import com.robinsmorton.rmappandroid.model.jobrequest.JobRequestResponse
import com.google.gson.Gson
import java.io.IOException
import java.lang.Exception

object JsonData {

    fun getJsonFromAssets(context: Context, jsonName: String): String? {
        return try {
            val inputStream = context.assets.open(jsonName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    fun getFollowedSites(context: Context): SitesResponse? {
        return try {
             Gson().fromJson(getJsonFromAssets(context, "followedsites.json"), SitesResponse::class.java)
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getJobRequests(context: Context): JobRequestResponse? {
        return try {
            Gson().fromJson(getJsonFromAssets(context, "jobrequests.json"), JobRequestResponse::class.java)
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getEstimateNumbers(context: Context): EstimateNumbersResponse? {
        return try {
            Gson().fromJson(getJsonFromAssets(context, "estimatenumberresponse.json"), EstimateNumbersResponse::class.java)
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}