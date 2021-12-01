package com.example.rmapplication.adapter

import android.content.Context
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
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.rmapplication.R
import com.example.rmapplication.databinding.GridItemBinding
import com.example.rmapplication.fragments.LobbyFragmentEventListener
import com.example.rmapplication.model.ButtonImage
import com.example.rmapplication.model.Item
import com.example.rmapplication.util.ImageUtil
import com.example.rmapplication.util.SessionManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONTokener

class LobbyAdapter(val ctx: Context, directoryList: List<Item>, val eventListener: LobbyFragmentEventListener) :
    ArrayAdapter<Item>(ctx, 0, directoryList) {

    val TAG = "LobbyAdapter"
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
            val buttonImageUrl = lobbyItem?.fields?.buttonimage?.let { getButtonImageUrl(it) }
            Log.d(TAG, "***Button image url - $buttonImageUrl")
            //Picasso.get().load(buttonImageUrl).resize(50, 50).into(binding.imageViewGridItem)
            //Glide.with(binding.imageViewGridItem.context).load(GlideUrl(buttonImageUrl)).into(binding.imageViewGridItem)

            /*Glide.with(context).load(GlideUrl(buttonImageUrl)).placeholder(R.drawable.rmlogo).listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "***Profile image load success ")
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "***Profile image load failed - " + e?.localizedMessage)
                        return false
                    }
                })
                .into(binding.imageViewGridItem)*/

            //ImageUtil.loadImageFromUrl(buttonImageUrl, binding.imageViewGridItem)
            loadImageFromDisplayName(lobbyItem?.fields?.Title, context)


        }

        private fun loadImageFromDisplayName(displayName: String?, context: Context) {
            if (displayName?.trim() == "HUB") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Job Request") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.ic_jobnumbers))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Preconstruction") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmiconsafety))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Human Resources") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rm_learning_culture))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Sustainability") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rm_sustainability))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Administration") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Policies and Procedures") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmpolicypro))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Job Numbers") {
                Glide.with(context).load(context.resources.getDrawable(R.drawable.rmworkforce))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Estimated Numbers") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmbudget))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Training Schedule") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmschedule))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "HR Information") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmcollab))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Safety Information") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmiconsafety))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Corporate Directory") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rmgov))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == "Applications") {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.rminfotech))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == context.resources.getString(R.string.waste_management)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.wastemanagement))
                    .into(binding.imageViewGridItem)
            } else if (displayName?.trim() == context.resources.getString(R.string.building_forward)) {
                Glide.with(context)
                    .load(context.resources.getDrawable(R.drawable.buildingforward))
                    .apply(RequestOptions().override(50, 50))
                    .centerCrop()
                    .into(binding.imageViewGridItem)
            }
        }

        fun getButtonImageUrl(jsonStringWithEscapeChars: String): String? {
            val jsonString = JSONTokener(jsonStringWithEscapeChars).nextValue().toString()
            val buttonImage: ButtonImage? = Gson().fromJson(jsonString, ButtonImage::class.java)

            return ""+buttonImage?.serverUrl?.trim()+buttonImage?.serverRelativeUrl?.trim()
        }
    }
}
