package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.ApplicationsGridItemBinding
import com.robinsmorton.rmappandroid.fragments.ApplicationsFragmentEventListener
import com.robinsmorton.rmappandroid.model.Value

class ApplicationsAdapter(private val ctx: Context, private var applicationsList: MutableList<Value>, val eventListener: ApplicationsFragmentEventListener) :
    ArrayAdapter<Value>(ctx, 0, applicationsList) {

    val TAG = "ApplicationsAdapter"
    private lateinit var binding: ApplicationsGridItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var viewHolder: ApplicationHolder
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.applications_grid_item,
                parent,
                false
            )
            viewHolder = ApplicationHolder(binding)
            viewHolder.itemView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ApplicationHolder
        }
        viewHolder.bindGridItem(getItem(position)?.fields?.Title)
        return viewHolder.itemView
    }

    override fun getCount(): Int {
        return applicationsList.count()
    }

    fun setAppList(appList: MutableList<Value>) {
        applicationsList.clear()
        applicationsList.addAll(appList)
        notifyDataSetChanged()
    }

    inner class ApplicationHolder(val binding: ApplicationsGridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var appName: String? = null

        init {
            binding.root.setOnClickListener {
                eventListener.onApplicationClickedEvent(appName)
            }
        }

        fun bindGridItem(application: String?) {
            this.appName = application
            application?.let { setItemDetails(it) }
        }

        private fun setItemDetails(displayName: String) {
            binding.textViewGridItem.text = displayName
            setImage(displayName)
        }

        private fun setImage(appName: String){
            when (appName) {
                ctx.resources.getString(R.string.ers_construction_products) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_ers))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.hh2) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_hh2))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.egnyte) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_egnyte))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.procore) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_procore))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.delta_dental) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_delta_dental))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.ring_central) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_ring_central))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.starleaf) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_starleaf))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.authy) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_authy))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.sap_concur) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_sap_concur))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.rm_ambassify) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_rm_ambassify))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.fidelity_netbenefits) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_fidelity_net))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.alabam_blue) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_alabama_blue))
                        .into(binding.imageViewGridItem)
                }
                ctx.resources.getString(R.string.unanet_crm) -> {
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.ic_unanet_crm))
                        .into(binding.imageViewGridItem)
                }
            }
        }
    }
}
