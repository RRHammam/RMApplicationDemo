package com.robinsmorton.rmappandroid.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.constants.Constants.SELECTED_APPLICATION_NAME
import com.robinsmorton.rmappandroid.constants.Constants.SELECTED_CORPORATE_USER
import com.robinsmorton.rmappandroid.databinding.FragmentApplicationInfoBinding
import com.robinsmorton.rmappandroid.databinding.FragmentCorporateDirectoryProfileBinding
import com.robinsmorton.rmappandroid.databinding.FragmentItSupportInfoBinding
import com.robinsmorton.rmappandroid.model.CorporateUser
import com.robinsmorton.rmappandroid.util.SessionManager

class ITSupportInfoFragment : BaseFragment() {
    val TAG = "ITSupportInfoFragment"
    private lateinit var binding: FragmentItSupportInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_it_support_info,
            container,
            false
        )
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
        handleActivityViews()
        setupBackPress()
        initData()
        setUpOnclickListener()
    }

    private fun initData() {
        binding.titleBar.imageViewBackButton.visibility = View.VISIBLE
        binding.titleBar.textViewSettings.text = resources.getString(R.string.it_support)
        binding.titleBar.textViewSettings.visibility = View.VISIBLE

        binding.layoutItSupportLink.textViewTitle.text = resources.getString(R.string.it_support_page)
        binding.layoutItSupportLink.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutItSupportLink.imageViewArrow.setBackgroundResource(R.drawable.ic_web)
        binding.layoutItSupportLink.imageViewIcon.visibility = View.GONE

        binding.layoutEmail.textViewTitle.text = resources.getString(R.string.email)
        binding.layoutEmail.textViewField.text = resources.getString(R.string.it_support_email)
        binding.layoutEmail.imageViewArrow.setImageResource(android.R.color.transparent)
        binding.layoutEmail.imageViewArrow.setBackgroundResource(R.drawable.ic_mail)
        binding.layoutEmail.imageViewIcon.visibility = View.GONE

        binding.layoutSupportNumber.textViewTitle.text = resources.getString(R.string.support_number)
        binding.layoutSupportNumber.textViewField.text = resources.getString(R.string.it_support_number)
        binding.layoutSupportNumber.imageViewIcon.visibility = View.GONE

    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(false)
        }
    }

    private fun setupBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            }
        )
    }

    private fun setUpOnclickListener() {
        binding.layoutItSupportLink.materialCardView.setOnClickListener {
            openBrowser(Constants.IT_SUPPORT_LINK)
        }
        binding.layoutEmail.materialCardView.setOnClickListener {
            sendEmail()
        }
        binding.layoutSupportNumber.materialCardView.setOnClickListener {
            openPhoneDialer()
        }
        binding.titleBar.imageViewBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    private fun sendEmail(){
        val emailText = binding.layoutEmail.textViewField.text.toString()
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "plain/text"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailText))
        requireContext().startActivity(
            Intent.createChooser(
                emailIntent,
                requireContext().getString(R.string.send_email)
            )
        )
    }

    private fun openPhoneDialer() {
        val callingIntent = Intent(
            Intent.ACTION_DIAL,
            Uri.fromParts(
                "tel",
                binding.layoutSupportNumber.textViewField.text.toString(),
                null
            )
        )
        requireContext().startActivity(callingIntent)
    }

}