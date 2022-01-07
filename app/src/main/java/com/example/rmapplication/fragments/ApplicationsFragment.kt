package com.example.rmapplication.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.rmapplication.R
import com.example.rmapplication.adapter.ApplicationsAdapter
import com.example.rmapplication.constants.Constants
import com.example.rmapplication.databinding.FragmentApplicationsBinding
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class ApplicationsFragment: BaseFragment(), ApplicationsFragmentEventListener {
    private lateinit var binding: FragmentApplicationsBinding
    private var adapter: ApplicationsAdapter? = null
    private var selectedItemForSearchType = ""
    private lateinit var categories: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_applications, container, false)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        categories = resources.getStringArray(R.array.application_categories)
        setDropDownAdapter()
        setApplicationsListAdapter(resources.getStringArray(R.array.all_apps).toMutableList())
        hideProgressBar()
    }

    private fun setApplicationsListAdapter(arrayOfApps: MutableList<String>) {
        adapter = this.context?.let { it1 -> ApplicationsAdapter(it1, arrayOfApps, this) }
        binding.gridViewApplications.adapter = adapter
    }


    private fun setDropDownAdapter() {
        val arrayAdapter = this.activity?.applicationContext?.let {
            ArrayAdapter(it, R.layout.dropdown_item, R.id.textView,categories )
        }
        binding.autoCompleteTextViewCategories.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewCategories.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(char: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(char: CharSequence?, start: Int, before: Int, count: Int) {
                selectedItemForSearchType = char.toString()
                resetSearchBarEndIcon()
                setupAppList()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun resetSearchBarEndIcon() {
        binding.textInputLayoutCategories.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
        binding.autoCompleteTextViewCategories.clearFocus()
    }

    private fun setupAppList() {
        when (selectedItemForSearchType) {
            categories[0] -> {
                adapter?.setAppList(resources.getStringArray(R.array.shopping_applications).toMutableList())
            }
            categories[1] -> {
                adapter?.setAppList(resources.getStringArray(R.array.payroll_and_time_entry_applications).toMutableList())
            }
            categories[2] -> {
                adapter?.setAppList(resources.getStringArray(R.array.data_storage_applications).toMutableList())
            }
            categories[3] -> {
                adapter?.setAppList(resources.getStringArray(R.array.construction_management_applications).toMutableList())
            }
            categories[4] -> {
                adapter?.setAppList(resources.getStringArray(R.array.finance_applications).toMutableList())
            }
            categories[5] -> {
                adapter?.setAppList(resources.getStringArray(R.array.medical_applications).toMutableList())
            }
            categories[6] -> {
                adapter?.setAppList(resources.getStringArray(R.array.communication_applications).toMutableList())
            }
            categories[7] -> {
                adapter?.setAppList(resources.getStringArray(R.array.social_media_applications).toMutableList())
            }
            categories[8] -> {
                adapter?.setAppList(resources.getStringArray(R.array.authentication_applications).toMutableList())
            }
            categories[9] -> {
                adapter?.setAppList(resources.getStringArray(R.array.expense_reports_applications).toMutableList())
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onApplicationClickedEvent(appName: String?) {
        appName?.let { openApp(it) }
    }

    private fun openApp(appName: String){
        when (appName) {
            resources.getString(R.string.ers_construction_products) -> {
                openApplicationOrLink(appName, Constants.ERS_CONSTRUCTION_PRODUCTS_LINK)
            }
            resources.getString(R.string.hh2) -> {
                openApplicationOrLink(appName, Constants.HH2_REMOTE_PAYROLL_LINK)
            }
            resources.getString(R.string.egnyte) -> {
                openApplicationOrLink(appName, Constants.EGNYTE_LINK)
            }
            resources.getString(R.string.procore) -> {
                openApplicationOrLink(appName, Constants.PROCORE_LINK)
                openBrowser(Constants.PROCORE_LINK)
            }
            resources.getString(R.string.delta_dental) -> {
                openApplicationOrLink(appName, Constants.DELTA_DENTA_LINKL)
            }
            resources.getString(R.string.ring_central) -> {
                openApplicationOrLink(appName, Constants.RINGCENTRAL_LINK)
            }
            resources.getString(R.string.starleaf) -> {
                openApplicationOrLink(appName, Constants.STARLEAF_LINK)
            }
            resources.getString(R.string.authy) -> {
                openApplicationOrLink(appName, Constants.AUTHY_LINK)
            }
            resources.getString(R.string.sap_concur) -> {
                openApplicationOrLink(appName, Constants.SAP_CONCUR_LINK)
            }
            resources.getString(R.string.rm_ambassify) -> {
                openApplicationOrLink(appName, Constants.RM_AMBASSIFY_LINK)
            }
            resources.getString(R.string.fidelity_netbenefits) -> {
                openApplicationOrLink(appName, Constants.FIDELITY_NET_BENEFITS_LINK)
            }
            resources.getString(R.string.alabam_blue) -> {
                openApplicationOrLink(appName, Constants.ALABAMA_BLUE_LINK)
            }
        }
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    private fun openApplicationOrLink(appName: String, link : String) {
        val appIntent = Intent(Intent.ACTION_MAIN, null)
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val packageManager = this.activity?.packageManager
        packageManager?.queryIntentActivities(appIntent, 0)?.forEach { resolveInfo ->
            if (resolveInfo.loadLabel(packageManager).equals(appName)) {
                runApp(packageManager, resolveInfo)
                return
            }
        }
        openBrowser(link)
    }

    private fun runApp(
        packageManager: PackageManager,
        resolveInfo: ResolveInfo
    ) {
        val launchIntent =
            packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.applicationInfo.packageName)
        startActivity(launchIntent)
    }

}


interface ApplicationsFragmentEventListener {
    fun onApplicationClickedEvent(appName: String?)
}