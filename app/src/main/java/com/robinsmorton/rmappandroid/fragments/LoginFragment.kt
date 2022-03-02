package com.robinsmorton.rmappandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.databinding.FragmentLoginBinding
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel.Companion.CMD_LOGIN_FAILURE
import com.robinsmorton.rmappandroid.viewmodel.LoginViewModel.Companion.CMD_LOGIN_SUCCESS

class LoginFragment : BaseFragment(), LoginPageEventListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return with(binding){
            eventListener = this@LoginFragment
            root
        }
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToEventCommands()
        (activity as MainActivity).getBottomNavView()?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        handleActivityViews()
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
}

interface LoginPageEventListener {
    fun onLoginClicked()
}