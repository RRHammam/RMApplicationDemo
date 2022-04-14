package com.robinsmorton.rmappandroid.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.constants.Constants.SELECTED_CORPORATE_USER
import com.robinsmorton.rmappandroid.databinding.FragmentCorporateDirectoryProfileBinding
import com.robinsmorton.rmappandroid.model.CorporateUser
import com.robinsmorton.rmappandroid.util.SessionManager

class CorporateDirectoryProfileFragment : BaseFragment() {
    val TAG = "CorporateDirectoryFragment"
    private lateinit var binding: FragmentCorporateDirectoryProfileBinding
    private var selectedCorporateUser: CorporateUser? = null
    private val url1 = "https://graph.microsoft.com/v1.0/users/"
    private val url2 = "/photo/\$value"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_corporate_directory_profile,
            container,
            false
        )
        return with(binding) {
            corporateUser = selectedCorporateUser
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unpackBundle()
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
        initData()
    }

    private fun unpackBundle() {
        arguments?.let {
            selectedCorporateUser = it.getParcelable(SELECTED_CORPORATE_USER)
        }
    }

    private fun initData() {
        binding.titleBar.imageViewCloseButton.visibility = View.VISIBLE
        binding.titleBar.imageViewBackButton.visibility = View.GONE
        binding.titleBar.imageViewCloseButton.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.textViewCorporateUserName.text = selectedCorporateUser?.displayName
        binding.textViewJobTitle.text = selectedCorporateUser?.jobTitle
        binding.layoutOfficeLocation.textViewTitle.text = "Office Location"
        binding.layoutOfficeLocation.textViewField.text = selectedCorporateUser?.officeLocation
        binding.layoutOfficeLocation.imageViewIcon.visibility = View.GONE
        binding.layoutOfficeLocation.imageViewArrow.visibility = View.GONE
        initMailData()
        initMobileNumberData()
        initBusinessVersionData()
        initProfileImage()
    }

    private fun initProfileImage() {
        val urlString = url1 + selectedCorporateUser?.id + url2
        Log.d(TAG, "Profile image url - $urlString")
        LazyHeaders.Builder().addHeader("Authorization", "Bearer ${SessionManager.access_token}")
        activity?.let {
            Glide.with(it).load(
                GlideUrl(
                    urlString,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer ${SessionManager.access_token}").build()
                )
            )
                .placeholder(R.drawable.rmlogo)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "Profile image load success ")
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "Profile image load failed - " + e?.localizedMessage)
                        return false
                    }
                })
                .into(binding.circleImageViewUser)
        }
    }

    private fun initBusinessVersionData() {
        if (!selectedCorporateUser?.businessPhones.isNullOrEmpty()) {
            binding.layoutBusinessNumber.textViewTitle.text = "Business Phone"
            selectedCorporateUser?.businessPhones?.get(0)
                ?.let { binding.layoutBusinessNumber.textViewField.text = it }
            binding.layoutBusinessNumber.imageViewIcon.setImageResource(R.drawable.ic_call)
            binding.layoutBusinessNumber.imageViewArrow.setImageResource(R.drawable.ic_rightarrow)
            binding.layoutBusinessNumber.imageViewIcon.visibility = View.VISIBLE
            binding.layoutBusinessNumber.imageViewArrow.visibility = View.VISIBLE
            binding.layoutBusinessNumber.materialCardView.setOnClickListener {
                val callingIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts(
                        "tel",
                        binding.layoutBusinessNumber.textViewField.text.toString(),
                        null
                    )
                )
                requireContext().startActivity(callingIntent)
            }
        } else {
            binding.layoutBusinessNumber.materialCardView.visibility = View.GONE
        }
    }

    private fun initMobileNumberData() {
        if (!selectedCorporateUser?.mobilePhone.isNullOrEmpty()) {
            binding.layoutMobileNumber.textViewTitle.text = "Mobile Phone"
            binding.layoutMobileNumber.textViewField.text = selectedCorporateUser?.mobilePhone
            binding.layoutMobileNumber.imageViewIcon.setImageResource(R.drawable.ic_call)
            binding.layoutMobileNumber.imageViewArrow.setImageResource(R.drawable.ic_rightarrow)
            binding.layoutMobileNumber.imageViewIcon.visibility = View.VISIBLE
            binding.layoutMobileNumber.imageViewArrow.visibility = View.VISIBLE
            binding.layoutMobileNumber.materialCardView.setOnClickListener {
                val callingIntent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.fromParts(
                        "tel",
                        binding.layoutMobileNumber.textViewField.text.toString(),
                        null
                    )
                )
                requireContext().startActivity(callingIntent)
            }
        } else {
            binding.layoutMobileNumber.materialCardView.visibility = View.GONE
        }
    }

    private fun initMailData() {
        if (!selectedCorporateUser?.mail.isNullOrEmpty()) {
            binding.layoutEmail.textViewTitle.text = "Email"
            binding.layoutEmail.textViewField.text = selectedCorporateUser?.mail
            binding.layoutEmail.imageViewIcon.setImageResource(R.drawable.ic_mail)
            binding.layoutEmail.imageViewArrow.setImageResource(R.drawable.ic_rightarrow)
            binding.layoutEmail.imageViewIcon.visibility = View.VISIBLE
            binding.layoutEmail.imageViewArrow.visibility = View.VISIBLE
            binding.layoutEmail.materialCardView.setOnClickListener {
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
        } else {
            binding.layoutEmail.materialCardView.visibility = View.GONE
        }
    }

}