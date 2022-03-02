package com.robinsmorton.rmappandroid.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


open class BaseFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun navigateUp() {
        if(!findNavController().navigateUp()) {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

}