package com.robinsmorton.rmappandroid.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.adapter.LobbyAdapter
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.databinding.FragmentLobbyBinding
import com.robinsmorton.rmappandroid.model.Item
import com.robinsmorton.rmappandroid.viewmodel.CorporateDirectoryViewModel
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel
import kotlinx.android.synthetic.main.item_loading_spinner.view.*


class LobbyFragment : BaseFragment(), LobbyFragmentEventListener {
    private lateinit var binding: FragmentLobbyBinding
    private lateinit var viewModel: LobbyViewModel
    private var adapter: LobbyAdapter? = null

    private val TAG = "LobbyFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lobby, container, false)
        viewModel = ViewModelProvider(this).get(LobbyViewModel::class.java)
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToRmAppListLiveData()
        subscribeToEventCommands()
        viewModel.getRmAppList()
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        handleActivityViews()
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(true)
        }
    }

    private fun subscribeToRmAppListLiveData() {
        viewModel.rmAppListLiveData.observe(viewLifecycleOwner, {
            adapter = this.context?.let { it1 -> LobbyAdapter(it1, it, this) }
            binding.gridViewLobby.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?) {
        when(lobbyGridItem?.fields?.Title?.trim()) {
            getString(R.string.corporate_directory) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_corporateDirectoryFragment)
            }
            getString(R.string.job_numbers) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_jobRequestFragment)
            }
            getString(R.string.estimated_numbers) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_estimateNumbersFragment)
            }
            getString(R.string.sustainability) -> {
                openBrowser(Constants.SUSTAINABILITY_LINK)
            }
            getString(R.string.waste_management) -> {
                openBrowser(Constants.WASTE_MANAGEMENT_LINK)
            }
            getString(R.string.building_forward) -> {
                openBrowser(Constants.BUILDING_FORWARD_LINK)
            }
            getString(R.string.hr_information) -> {
                openBrowser(Constants.HR_INFORMATION_LINK)
            }
            getString(R.string.safety_information) -> {
                openBrowser(Constants.SAFETY_INFORMATION_LINK)
            }
            getString(R.string.applications) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_applicationsFragment)
            }
            getString(R.string.policies_and_procedures) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_policiesAndProceduresFragment)
            }
        }
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                CorporateDirectoryViewModel.cmd_show_loading_sign -> showProgressBar()

                CorporateDirectoryViewModel.cmd_hide_loading_sign -> hideProgressBar()
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
}

interface LobbyFragmentEventListener {
    fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?)
}