package com.robinsmorton.rmappandroid.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.databinding.GridItemBinding
import com.robinsmorton.rmappandroid.fragments.LobbyFragmentEventListener
import com.robinsmorton.rmappandroid.model.ButtonImage
import com.robinsmorton.rmappandroid.model.Item
import com.google.gson.Gson
import org.json.JSONTokener

class LobbyAdapter(val ctx: Context, val lobbyList: MutableList<Item>, val eventListener: LobbyFragmentEventListener) :
    RecyclerView.Adapter<LobbyAdapter.LobbyItemHolder>() {

    val TAG = "LobbyAdapter"
    private lateinit var binding: GridItemBinding

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
            lobbyItem?.fields?.buttonimage?.let { getButtonImageUrl(it) }
            setItemDetails(lobbyItem?.fields?.Title, context)
        }

        private fun setItemDetails(displayName: String?, context: Context) {
            binding.constraintLayoutParentLayout.setBackgroundColor(context.resources.getColor(R.color.white, context.theme))
            binding.materialCardViewLobbyItem.setBackgroundColor(context.resources.getColor(R.color.white, context.theme))
            when {
                displayName?.trim() == context.getString(R.string.project_numbers) -> {
                    binding.textViewGridItem.text = context.getString(R.string.job_numbers_text)
                    Glide.with(context).load(ContextCompat.getDrawable( context, R.drawable.ic_jobnumbers))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.sustainability) -> {
                    binding.textViewGridItem.text = context.getString(R.string.sustainability)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.rm_sustainability))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.policies_and_procedures) -> {
                    binding.textViewGridItem.text = context.getString(R.string.policies_and_procedures_text)
                    Glide.with(context).load(ContextCompat.getDrawable(context, R.drawable.rmpolicypro))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.estimate_numbers) -> {
                    binding.textViewGridItem.text = context.getString(R.string.estimated_numbers_text)
                    Glide.with(context).load(ContextCompat.getDrawable( context, R.drawable.rmbudget))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.time_entry_paystubs) -> {
                    binding.textViewGridItem.text = context.getString(R.string.time_entry_paystubs_text)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.rmcollab))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.safety_information) -> {
                    binding.textViewGridItem.text = context.getString(R.string.safety_information_text)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.rmiconsafety))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.corporate_directory) -> {
                    binding.textViewGridItem.text = context.getString(R.string.corporate_directory_text)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.rmgov))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.getString(R.string.applications) -> {
                    binding.textViewGridItem.text = context.getString(R.string.applications)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.rminfotech))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.resources.getString(R.string.waste_management) -> {
                    binding.textViewGridItem.text = context.getString(R.string.waste_management_text)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.wastemanagement))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.resources.getString(R.string.building_forward) -> {
                    binding.textViewGridItem.text = context.getString(R.string.building_forward_text)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.ic_bf_icon))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.resources.getString(R.string.it_support) -> {
                    binding.textViewGridItem.text = context.getString(R.string.it_support)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.ic_it_support))
                        .into(binding.imageViewGridItem)
                }
                displayName?.trim() == context.resources.getString(R.string.rm_web) -> {
                    binding.textViewGridItem.text = context.getString(R.string.training_excellence)
                    Glide.with(context)
                        .load(ContextCompat.getDrawable( context, R.drawable.ic_training_excellence))
                        .into(binding.imageViewGridItem)
                }
            }
        }

        private fun getButtonImageUrl(jsonStringWithEscapeChars: String): String {
            val jsonString = JSONTokener(jsonStringWithEscapeChars).nextValue().toString()
            val buttonImage: ButtonImage? = Gson().fromJson(jsonString, ButtonImage::class.java)

            return ""+buttonImage?.serverUrl?.trim()+buttonImage?.serverRelativeUrl?.trim()
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

    fun moveItem(from: Int, to: Int) {
        val fromEntry = lobbyList[from]
        lobbyList.removeAt(from)
        lobbyList.add(to, fromEntry)
    }
}
