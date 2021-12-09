package com.example.rmapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.rmapplication.R
import com.example.rmapplication.activities.MainActivity
import com.example.rmapplication.databinding.FragmentLoginBinding
import com.example.rmapplication.viewmodel.LoginViewModel
import com.example.rmapplication.viewmodel.LoginViewModel.Companion.CMD_LOGIN_FAILURE
import com.example.rmapplication.viewmodel.LoginViewModel.Companion.CMD_LOGIN_SUCCESS

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

    fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.bringToFront()
    }

    fun hideProgressBar() {
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