package com.robinsmorton.rmappandroid.fragments

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.databinding.FragmentLoginBinding
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel.Companion.CMD_LOGIN_FAILURE
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel.Companion.CMD_LOGIN_SUCCESS
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginFragment : BaseFragment(), LoginPageEventListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return with(binding){
            eventListener = this@LoginFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        (activity as MainActivity).getBottomNavView()?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        hideProgressBar()
    }

    override fun onLoginClicked() {
        showProgressBar()
        // Attempt silent sign in first
        // if this fails, the callback will handle doing
        // interactive sign in
        viewModel.doSilentSignIn(true, requireActivity())
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.bringToFront()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    private fun subscribeToEventCommands(){
        viewModel.eventCommand.observe(viewLifecycleOwner, {
            when(it) {
                CMD_LOGIN_SUCCESS -> {
                    findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
                }
                CMD_LOGIN_FAILURE -> {

                }
            }
        })
    }

    private fun generateHashKey() {
        /*try {
            val info: PackageInfo = activity.getPackageManager().getPackageInfo("com.apps.sonictonic", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d(
                    "KeyHash", "KeyHash:" + Base64.encodeToString(
                        md.digest(),
                        Base64.DEFAULT
                    )
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }*/
    }
}

interface LoginPageEventListener {
    fun onLoginClicked()
}