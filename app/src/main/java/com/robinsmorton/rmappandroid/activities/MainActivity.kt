package com.robinsmorton.rmappandroid.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.robinsmorton.rmappandroid.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printAppHashKeyInLogs()
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = nav_host_fragment as NavHostFragment
        NavigationUI.setupWithNavController(bottom_navView, navHostFragment.navController)
        bottom_navView.setOnNavigationItemReselectedListener { false }

    }

    public fun getBottomNavView(): BottomNavigationView? {
        return bottom_navView
    }

    private fun printAppHashKeyInLogs() {
        try {
            val info = packageManager.getPackageInfo(
                "com.robinsmorton.rmappandroid",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("***hash key", something)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    /*override fun onBackPressed() {
        //Do nothing
    }*/
}