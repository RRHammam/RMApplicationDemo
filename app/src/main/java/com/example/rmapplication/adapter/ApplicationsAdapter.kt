package com.example.rmapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rmapplication.R
import com.example.rmapplication.databinding.ApplicationsGridItemBinding
import com.example.rmapplication.fragments.ApplicationsFragmentEventListener

class ApplicationsAdapter(private val ctx: Context, private var applicationsList: MutableList<String>, val eventListener: ApplicationsFragmentEventListener) :
    ArrayAdapter<String>(ctx, 0, applicationsList) {

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
        viewHolder.bindGridItem(getItem(position), context)
        return viewHolder.itemView
    }

    override fun getCount(): Int {
        return applicationsList.count()
    }

    fun setAppList(appList: MutableList<String>) {
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

        fun bindGridItem(application: String?, context: Context) {
            this.appName = application
            setItemDetails(application, context)
        }

        private fun setItemDetails(displayName: String?, context: Context) {
            binding.textViewGridItem.text = displayName
            Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.rminfotech))
                .into(binding.imageViewGridItem)
        }
    }
}
