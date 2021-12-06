package com.example.rmapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rmapplication.LobbyItemSelectedEvent
import com.example.rmapplication.R
import com.example.rmapplication.activities.MainActivity
import com.example.rmapplication.adapter.LobbyAdapter
import com.example.rmapplication.databinding.FragmentLobbyBinding
import com.example.rmapplication.model.Item
import com.example.rmapplication.viewmodel.CorporateDirectoryViewModel
import com.example.rmapplication.viewmodel.LobbyViewModel
import kotlinx.android.synthetic.main.item_loading_spinner.view.*
import org.greenrobot.eventbus.Subscribe


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
        (activity as MainActivity).geBottomNavView()?.visibility = View.VISIBLE
    }

    fun subscribeToDirectoryListLiveData() {
        /* viewModel.directoryListLiveData.observe(viewLifecycleOwner, {
             adapter = this.context?.let { it1 -> LobbyAdapter(it1, it) }
             binding.gridViewLobby.adapter = adapter
             adapter?.notifyDataSetChanged()
         })*/
    }

    fun subscribeToRmAppListLiveData() {
        viewModel.rmAppListLiveData.observe(viewLifecycleOwner, {
            adapter = this.context?.let { it1 -> LobbyAdapter(it1, it, this) }
            binding.gridViewLobby.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?) {
        Log.d(TAG, "onLobbyGridItemClickedEvent called ${lobbyGridItem?.fields?.Title}")
        if(lobbyGridItem?.fields?.Title?.trim() == "Corporate Directory"){
            findNavController().navigate(R.id.action_lobbyFragment_to_corporateDirectoryFragment)
        } else if(lobbyGridItem?.fields?.Title?.trim() == "Job Numbers") {
            findNavController().navigate(R.id.action_lobbyFragment_to_jobRequestFragment)
        } else if(lobbyGridItem?.fields?.Title?.trim() == getString(R.string.estimated_numbers)) {
            findNavController().navigate(R.id.action_lobbyFragment_to_estimateNumbersFragment)
        }
    }

    fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                CorporateDirectoryViewModel.cmd_show_loading_sign -> showProgressBar()

                CorporateDirectoryViewModel.cmd_hide_loading_sign -> hideProgressBar()
            }
        })
    }

    fun showProgressBar() {
        Log.d(TAG, "***showProgressBar()")
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        Log.d(TAG, "***hideProgressBar()")
        binding.progressBar.visibility = View.GONE
    }
}

interface LobbyFragmentEventListener {
    fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?)
}