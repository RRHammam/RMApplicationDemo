package com.robinsmorton.rmappandroid.util

object SessionManager {
    //private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    var access_token: String?  = null

    /**
     * Function to save auth token
     */
    /*fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }*/

    /**
     * Function to fetch auth token
     */
    /*fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }*/
}