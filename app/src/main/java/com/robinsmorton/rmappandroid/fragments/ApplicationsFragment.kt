package com.robinsmorton.rmappandroid.fragments

import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.adapter.ApplicationsAdapter
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.databinding.FragmentApplicationsBinding
import com.robinsmorton.rmappandroid.model.Value
import com.robinsmorton.rmappandroid.viewmodel.ApplicationsViewModel
import com.robinsmorton.rmappandroid.viewmodel.ApplicationsViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.ApplicationsViewModel.Companion.cmd_show_loading_sign
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

class ApplicationsFragment: BaseFragment(), ApplicationsFragmentEventListener {
    private val TAG = "ApplicationsFragment"
    private lateinit var binding: FragmentApplicationsBinding
    private var adapter: ApplicationsAdapter? = null
    private var selectedItemForSearchType = ""
    private lateinit var categories: MutableList<String>
    private lateinit var viewModel: ApplicationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_applications, container, false)
        viewModel = ViewModelProvider(this).get(ApplicationsViewModel::class.java)
        return with(binding){
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        handleActivityViews()
        subscribeToEstimateNumberListLiveData()
        subscribeToEventCommands()
        viewModel.getListOfApplications()
        binding.titleBar.imageViewBackButton.setOnClickListener {
            navigateUp()
        }
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(false)
        }
    }

    private fun subscribeToEstimateNumberListLiveData() {
        viewModel.applicationsListLiveData.observe(viewLifecycleOwner, {
            setApplicationsListAdapter(it)
            categories = viewModel.getCategoriesFromMainList()
            categories.add(0,"Select Application Category")
            setDropDownAdapter()
        })
    }


    private fun setApplicationsListAdapter(appList: MutableList<Value>) {
        adapter = this.context?.let { it1 -> ApplicationsAdapter(it1, appList, this) }
        binding.gridViewApplications.adapter = adapter
    }


    private fun setDropDownAdapter() {
        val arrayAdapter = this.activity?.applicationContext?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, categories)
        }
        arrayAdapter?.setDropDownViewResource(R.layout.dropdown_item)
        binding.spinnerApplication.adapter = arrayAdapter

        binding.spinnerApplication.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position != 0) {
                    selectedItemForSearchType = categories[position]
                    setupAppList()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    private fun setupAppList() {
        adapter?.setAppList(viewModel.getApplicationListForSelectedCategory(selectedItemForSearchType))
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()
            }
        })
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
        appName?.let { handleOnAppClickEvent(it) }
    }

    private fun handleOnAppClickEvent(appName: String){
        when (appName) {
            resources.getString(R.string.ers_construction_products) -> {
                doOnAppClickAction(appName, Constants.ERS_CONSTRUCTION_PRODUCTS_LINK)
            }
            resources.getString(R.string.hh2) -> {
                doOnAppClickAction(appName, Constants.HH2_REMOTE_PAYROLL_LINK)
            }
            resources.getString(R.string.egnyte) -> {
                doOnAppClickAction(appName, Constants.EGNYTE_LINK)
            }
            resources.getString(R.string.procore) -> {
                doOnAppClickAction(appName, Constants.PROCORE_LINK)
            }
            resources.getString(R.string.delta_dental) -> {
                doOnAppClickAction(appName, Constants.DELTA_DENTA_LINKL)
            }
            resources.getString(R.string.ring_central) -> {
                doOnAppClickAction(appName, Constants.RINGCENTRAL_LINK)
            }
            resources.getString(R.string.starleaf) -> {
                doOnAppClickAction(appName, Constants.STARLEAF_LINK)
            }
            resources.getString(R.string.authy) -> {
                doOnAppClickAction(appName, Constants.AUTHY_LINK)
            }
            resources.getString(R.string.sap_concur) -> {
                doOnAppClickAction(appName, Constants.SAP_CONCUR_LINK)
            }
            resources.getString(R.string.rm_ambassify) -> {
                doOnAppClickAction(appName, Constants.RM_AMBASSIFY_LINK)
            }
            resources.getString(R.string.fidelity_netbenefits) -> {
                doOnAppClickAction(appName, Constants.FIDELITY_NET_BENEFITS_LINK)
            }
            resources.getString(R.string.alabam_blue) -> {
                doOnAppClickAction(appName, Constants.ALABAMA_BLUE_LINK)
            }
            resources.getString(R.string.unanet_crm) -> {
                doOnAppClickAction(appName, Constants.UNANET_CRM_LINK)
            }
            resources.getString(R.string.sap_success_factor) -> {
                val bundle = Bundle()
                bundle.putString(Constants.SELECTED_APPLICATION_NAME,  resources.getString(R.string.sap_success_factor))
                findNavController().navigate(R.id.action_applicationsFragment_to_applicationInfoFragment, bundle)
            }
            resources.getString(R.string.ricky_kalmon) -> {
                val bundle = Bundle()
                bundle.putString(Constants.SELECTED_APPLICATION_NAME, resources.getString(R.string.ricky_kalmon))
                findNavController().navigate(R.id.action_applicationsFragment_to_applicationInfoFragment, bundle)
            }
        }
    }

    private fun doOnAppClickAction(appName: String, link: String) {
        val resolveInfo = getInstalledAppResolveInfo(appName)
        if (resolveInfo != null) {
            runApp(resolveInfo)
        } else {
            openBrowser(link)
        }
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
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

}


interface ApplicationsFragmentEventListener {
    fun onApplicationClickedEvent(appName: String?)
}