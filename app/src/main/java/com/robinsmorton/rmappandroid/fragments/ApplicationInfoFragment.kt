package com.robinsmorton.rmappandroid.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.constants.Constants.SELECTED_APPLICATION_NAME
import com.robinsmorton.rmappandroid.constants.Constants.SELECTED_CORPORATE_USER
import com.robinsmorton.rmappandroid.databinding.FragmentApplicationInfoBinding
import com.robinsmorton.rmappandroid.databinding.FragmentCorporateDirectoryProfileBinding
import com.robinsmorton.rmappandroid.model.CorporateUser
import com.robinsmorton.rmappandroid.util.SessionManager

class ApplicationInfoFragment : BaseFragment() {
    val TAG = "CorporateDirectoryFragment"
    private lateinit var binding: FragmentApplicationInfoBinding
    private var selectedAppName: String? = null
    private val url1 = "https://graph.microsoft.com/v1.0/users/"
    private val url2 = "/photo/\$value"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_application_info,
            container,
            false
        )
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unpackBundle()
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
        handleActivityViews()
        setupBackPress()
        initData()
        setUpOnclickListener()
    }

    private fun unpackBundle() {
        arguments?.let {
            selectedAppName = it.getString(SELECTED_APPLICATION_NAME)
        }
    }

    private fun initData() {
        binding.titleBar.imageViewBackButton.visibility = View.VISIBLE
        if(selectedAppName == resources.getString(R.string.sap_success_factor)) {
            initSapSuccessFactor()
        } else if(selectedAppName == resources.getString(R.string.ricky_kalmon)) {
            initRickyKalmon()
        }
    }

    private fun initSapSuccessFactor() {
        binding.circleImageViewApp.setImageResource(R.drawable.ic_sap_successfactor)
        binding.textViewAppTitle.text = selectedAppName
        binding.textViewAppSubTitle.text = ""
        binding.layoutPlaystore.imageViewIcon.visibility = View.GONE
        binding.layoutPlaystore.textViewTitle.text = "Playstore"
        binding.layoutPlaystore.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutPlaystore.imageViewArrow.setBackgroundResource(R.drawable.ic_web)
        binding.layoutCompanyCode.imageViewIcon.visibility = View.GONE
        binding.layoutCompanyCode.textViewTitle.text = "Company Code"
        binding.layoutCompanyCode.textViewField.text = "robinsandm"
        binding.layoutCompanyCode.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutCompanyCode.imageViewArrow.setBackgroundResource(R.drawable.ic_copy)
    }

    private fun initRickyKalmon() {
        binding.circleImageViewApp.setImageResource(R.drawable.ic_rickykalmon)
        binding.textViewAppTitle.text = selectedAppName
        binding.textViewAppSubTitle.text = "Meditation and Mindfulness"
        binding.layoutPlaystore.imageViewIcon.visibility = View.GONE
        binding.layoutPlaystore.textViewTitle.text = "Playstore"
        binding.layoutPlaystore.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutPlaystore.imageViewArrow.setBackgroundResource(R.drawable.ic_web)
        binding.layoutCompanyCode.textViewTitle.text = "Company Code"
        binding.layoutCompanyCode.textViewField.text = "RMCONNECT"
        binding.layoutCompanyCode.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutCompanyCode.imageViewArrow.setBackgroundResource(R.drawable.ic_copy)
        binding.layoutCompanyCode.imageViewIcon.visibility = View.GONE
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(false)
        }
    }

    private fun setupBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            }
        )
    }

    private fun setUpOnclickListener() {
        binding.layoutPlaystore.imageViewArrow.setOnClickListener {
            if(selectedAppName == resources.getString(R.string.sap_success_factor)) {
                onClickPlayStoreForSapSuccessFactor()
            } else  if(selectedAppName == resources.getString(R.string.ricky_kalmon)) {
                onClickPlayStoreForRickyKalmon()
            }
        }

        binding.layoutCompanyCode.imageViewArrow.setOnClickListener {
            val clipboard = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Text", binding.layoutCompanyCode.textViewField.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(activity, "Company code copied to clipboard.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onClickPlayStoreForRickyKalmon() {
        selectedAppName?.let { doOnAppClickAction(it, Constants.RICKY_KALMON_LINK ) }
    }

    private fun onClickPlayStoreForSapSuccessFactor() {
        selectedAppName?.let { doOnAppClickAction(it, Constants.SAP_SUCCESS_FACTORS_LINK ) }
    }

    private fun doOnAppClickAction(appName: String, link: String) {
        val resolveInfo = getInstalledAppResolveInfo(appName)
        if (resolveInfo != null) {
            runApp(resolveInfo)
        } else {
            openBrowser(link)
        }
    }

    private fun getInstalledAppResolveInfo(appName: String): ResolveInfo? {
        val appIntent = Intent(Intent.ACTION_MAIN, null)
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = this.activity?.packageManager
        packageManager?.queryIntentActivities(appIntent, 0)?.forEach { resolveInfo ->
            Log.d(TAG, "***Installed apps : ${resolveInfo.loadLabel(packageManager)}")
            if (resolveInfo.loadLabel(packageManager).equals(appName)) {
                return resolveInfo
            }
        }
        return null
    }

    private fun runApp(resolveInfo: ResolveInfo) {
        val launchIntent = this.activity?.packageManager?.getLaunchIntentForPackage(resolveInfo.activityInfo.applicationInfo.packageName)
        startActivity(launchIntent)
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

}