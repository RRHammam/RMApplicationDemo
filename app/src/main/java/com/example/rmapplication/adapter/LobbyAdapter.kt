package com.example.rmapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rmapplication.LobbyItemSelectedEvent
import com.example.rmapplication.R
import com.example.rmapplication.databinding.GridItemBinding
import com.example.rmapplication.fragments.LobbyFragmentEventListener
import com.example.rmapplication.model.Item
import org.greenrobot.eventbus.EventBus

class LobbyAdapter(
    val ctx: Context,
    directoryList: List<Item>,
    val eventListener: LobbyFragmentEventListener
) :
    ArrayAdapter<Item>(ctx, 0, directoryList) {

    private lateinit var binding: GridItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var viewHolder: LobbyItemHolder
        if (convertView == null) {
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.grid_item,
                parent,
                false
            )
            viewHolder = LobbyItemHolder(binding)
            viewHolder.itemView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as LobbyItemHolder
        }
        viewHolder.bindGridItem(getItem(position), context)
        return viewHolder.itemView
    }


    inner class LobbyItemHolder(val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var lobbyItem: Item? = null

        init {
            binding.root.setOnClickListener {
                eventListener.onLobbyGridItemClickedEvent(lobbyItem)
            }
        }

        fun bindGridItem(lobbyItem: Item?, context: Context) {
            this.lobbyItem = lobbyItem
            binding.textViewGridItem.text = lobbyItem?.fields?.Title
            loadImageFromDisplayName(lobbyItem?.fields?.Title?.trim(), context)
        }

        private fun loadImageFromDisplayName(displayName: String?, context: Context) {
            if (displayName == "HUB") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Job Request") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.ic_jobnumbers))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Preconstruction") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmiconsafety))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Human Resources") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rm_learning_culture))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Sustainability") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rm_sustainability))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Administration") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Policies and Procedures") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmpolicypro))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Job Numbers") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmworkforce))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Estimated Numbers") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmbudget))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Training Schedule") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmschedule))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "HR Information") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmcollab))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Safety Information") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmiconsafety))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Safety Information") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmiconsafety))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Corporate Directory") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmgov))
                    .into(binding.imageViewGridItem)
            } else if (displayName == "Applications") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            }
        }
    }
}
