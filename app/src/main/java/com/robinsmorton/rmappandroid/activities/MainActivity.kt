package com.robinsmorton.rmappandroid.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.robinsmorton.rmappandroid.R
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : BaseActivity() {
    private var titleBarImageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        titleBarImageView = findViewById(R.id.imageView_rm_logo_titleBar)
        printAppHashKeyInLogs()
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = nav_host_fragment as NavHostFragment
        NavigationUI.setupWithNavController(bottom_navView, navHostFragment.navController)
        bottom_navView.setOnNavigationItemReselectedListener { false }

    }

    fun getBottomNavView(): BottomNavigationView? {
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

    fun showAppBar(flag: Boolean) {
        titleBarImageView?.visibility = if(flag) View.VISIBLE else View.GONE
    }
}