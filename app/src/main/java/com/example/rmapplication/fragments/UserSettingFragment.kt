package com.example.rmapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.rmapplication.R
import com.example.rmapplication.constants.Constants
import com.example.rmapplication.databinding.FragmentUserSettingsBinding

class UserSettingsFragment : BaseFragment(), UserSettingsPageEventListener {

    private lateinit var binding: FragmentUserSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_settings, container, false)
        return with(binding) {
            eventListener = this@UserSettingsFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewUserName.text = Constants.userDetails?.displayName
        if (Constants.userDetails?.photo != null) {
            Glide.with(this).load(Constants.userDetails?.photo).into(binding.circleImageViewUser)
        } else {
            binding.textViewUserInitials.visibility = View.VISIBLE
            binding.textViewUserInitials.text = getUserInitials()
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
        Toast.makeText(this.context, "Logout button clicked!!!", Toast.LENGTH_SHORT).show()
    }
}

interface UserSettingsPageEventListener {
    fun onLogoutClicked()
}