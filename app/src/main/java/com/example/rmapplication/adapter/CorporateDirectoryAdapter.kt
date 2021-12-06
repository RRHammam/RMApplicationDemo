package com.example.rmapplication.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.rmapplication.R
import com.example.rmapplication.databinding.CorporateUserItemBinding
import com.example.rmapplication.databinding.GridItemBinding
import com.example.rmapplication.model.CorporateUser
import com.example.rmapplication.model.Item
import com.example.rmapplication.model.Site
import com.example.rmapplication.util.SessionManager

class CorporateDirectoryAdapter(val context: Context, var corporateDirectoryList: MutableList<CorporateUser>) :
    RecyclerView.Adapter<CorporateDirectoryAdapter.CorporateUserItemHolder>() {

    private val TAG = "CorporateDirectoryAdapter"
    private lateinit var binding: CorporateUserItemBinding
    private val url1 = "https://graph.microsoft.com/v1.0/users/"
    private val url2 = "/photo/\$value"

    inner class CorporateUserItemHolder(val binding: CorporateUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindListItem(user: CorporateUser?) {
            binding.textViewCorporateUserName.text = user?.displayName
            binding.textViewCorporateUserDesignation.text = user?.jobTitle
            if(!user?.mail.isNullOrEmpty()) {
                binding.textViewCorporateUserEmail.text = user?.mail
                binding.textViewCorporateUserEmail.setOnClickListener {
                    val emailText = binding.textViewCorporateUserEmail.text.toString()
                    val emailIntent = Intent(Intent.ACTION_SEND)
                    emailIntent.type = "plain/text"
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailText))
                    context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_email)))
                }
            } else {
                binding.textViewCorporateUserEmail.visibility = View.GONE
            }

            if(!user?.mobilePhone.isNullOrEmpty()) {
                binding.textViewCorporateUserPhoneNumber.text = user?.mobilePhone
                binding.textViewCorporateUserPhoneNumber.setOnClickListener {
                    val callingIntent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", binding.textViewCorporateUserPhoneNumber.text.toString(), null))
                    context.startActivity(callingIntent)
                }
            } else {
                binding.textViewCorporateUserPhoneNumber.visibility = View.GONE
            }

            val urlString = url1+user?.id+url2
            Log.d(TAG, "Profile image url - $urlString")
            LazyHeaders.Builder().addHeader("Authorization", "Bearer ${SessionManager.access_token}")
            Glide.with(context).load(
                GlideUrl(
                    urlString,
                    LazyHeaders.Builder()
                        .addHeader("Authorization", "Bearer ${SessionManager.access_token}")
                        .build()
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
                .into(binding.imageViewCorporateUser)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CorporateUserItemHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.corporate_user_item,
            parent,
            false
        )
        return  CorporateUserItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CorporateUserItemHolder, position: Int) {
        holder.bindListItem(corporateDirectoryList[position])
    }

    override fun getItemCount(): Int {
        return corporateDirectoryList.size
    }

    fun addDataInList(updatedList: MutableList<CorporateUser>){
        corporateDirectoryList.addAll(updatedList)
        notifyDataSetChanged()
    }

    fun clearAndUpdateList(updatedList: MutableList<CorporateUser>?) {
        updatedList?.let {
            corporateDirectoryList.clear()
            corporateDirectoryList.addAll(it)
            notifyDataSetChanged()
        }
    }
}
