package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.databinding.FragmentUserSettingsBinding
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel
import com.robinsmorton.rmappandroid.viewmodel.UserSettingsViewModel

class UserSettingsFragment : BaseFragment(), UserSettingsPageEventListener{

    private lateinit var binding: FragmentUserSettingsBinding
    private lateinit var viewModel: UserSettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_settings, container, false)
        viewModel = ViewModelProvider(this).get(UserSettingsViewModel::class.java)
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        initialize()
    }

    private fun initialize() {
        binding.textViewUserName.text = Constants.userDetails?.displayName
        binding.textViewJobTitle.text = Constants.userDetails?.jobTitle
        if (Constants.userDetails?.photo != null) {
            Glide.with(this).load(Constants.userDetails?.photo).into(binding.circleImageViewUser)
        } else {
            binding.textViewUserInitials.visibility = View.VISIBLE
            binding.textViewUserInitials.text = getUserInitials()
        }
        binding.imageViewBackButton.setOnClickListener {
            (activity as MainActivity).getBottomNavView()?.selectedItemId = R.id.lobbyFragment
        }
        binding.buttonLogout.setOnClickListener {
            onLogoutClicked()
        }
    }

    private fun getUserInitials(): String {
        var userInitials = ""
        Constants.userDetails?.displayName.let { name ->
            val namelist = name?.split(" ")
            namelist?.forEach {
                if (it.isNotEmpty()) {
                    userInitials += it[0]
                }
            }
        }
        return userInitials
    }

    override fun onLogoutClicked() {
        viewModel.doLogout()
    }

    private fun subscribeToEventCommands(){
        viewModel.eventCommand.observe(viewLifecycleOwner, {
            when(it) {
                UserSettingsViewModel.CMD_LOGOUT_SUCCESS-> {
                    findNavController().navigate(R.id.action_userSettingsFragment_to_loginFragment)
                }
                UserSettingsViewModel.CMD_LOGOUT_FAILURE -> {
                    Toast.makeText(activity?.application, "Error doing logout.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}

interface UserSettingsPageEventListener {
    fun onLogoutClicked()
}