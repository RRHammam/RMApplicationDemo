package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.GridItemBinding
import com.robinsmorton.rmappandroid.fragments.GuestLobbyFragmentEventListener

class GuestLobbyAdapter(val ctx: Context, private val lobbyList: Array<String>, val eventListener: GuestLobbyFragmentEventListener) :
    RecyclerView.Adapter<GuestLobbyAdapter.LobbyItemHolder>() {

    val TAG = "GuestLobbyAdapter"
    private lateinit var binding: GridItemBinding

    inner class LobbyItemHolder(val binding: GridItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var lobbyItem: String? = null

        init {
            binding.root.setOnClickListener {
                eventListener.onGuestLobbyGridItemClickedEvent(lobbyItem)
            }
        }

        fun bindGridItem(lobbyItem: String?, context: Context) {
            this.lobbyItem = lobbyItem
            binding.textViewGridItem.text = lobbyItem
            setImage(lobbyItem, context)
        }

        private fun setImage(lobbyItem: String?, context: Context) {
            when (lobbyItem) {
                ctx.getString(R.string.about_us) -> {
                    Glide.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.ic_home_rm))
                        .into(binding.imageViewGridItem)
                }
                ctx.getString(R.string.building_forward) -> {
                    Glide.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.ic_bf_icon))
                        .into(binding.imageViewGridItem)
                }
                ctx.getString(R.string.join_our_team) -> {
                    Glide.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.ic_jobnumbers))
                        .into(binding.imageViewGridItem)
                }
                ctx.getString(R.string.contact_us) -> {
                    Glide.with(context)
                        .load(ContextCompat.getDrawable(context, R.drawable.ic_contactus))
                        .into(binding.imageViewGridItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LobbyItemHolder {
        binding = DataBindingUtil.inflate(
                LayoutInflater.from(ctx),
                R.layout.grid_item,
                parent,
                false
            )
        return LobbyItemHolder(binding)
    }

    override fun onBindViewHolder(holder: LobbyItemHolder, position: Int) {
        val listItem = lobbyList[position]
        holder.bindGridItem(listItem, ctx)
    }

    override fun getItemCount(): Int {
        return lobbyList.size
    }

}
