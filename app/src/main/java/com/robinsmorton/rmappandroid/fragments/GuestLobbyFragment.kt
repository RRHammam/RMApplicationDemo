package com.robinsmorton.rmappandroid.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.robinsmorton.rmappandroid.R
import com.robinsmorton.rmappandroid.activities.MainActivity
import com.robinsmorton.rmappandroid.adapter.GuestLobbyAdapter
import com.robinsmorton.rmappandroid.adapter.LobbyAdapter
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.databinding.FragmentGuestLobbyBinding
import com.robinsmorton.rmappandroid.model.Item
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel.Companion.cmd_show_loading_sign
import kotlinx.android.synthetic.main.fragment_lobby.*
import kotlinx.android.synthetic.main.item_loading_spinner.view.*


class GuestLobbyFragment : BaseFragment(), GuestLobbyFragmentEventListener {
    private lateinit var binding: FragmentGuestLobbyBinding
    private var adapter: GuestLobbyAdapter? = null

    private val TAG = "GuestLobbyFragment"

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val adapter = recyclerView_grid_lobby.adapter as LobbyAdapter
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition

                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }
        }
        ItemTouchHelper(simpleItemTouchCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_guest_lobby, container, false)
        return with(binding) {
            eventListener = this@GuestLobbyFragment
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        (activity as MainActivity).getBottomNavView()?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        handleActivityViews()
    }

    private fun handleActivityViews() {
        activity?.let {
            (it as MainActivity).showAppBar(true)
        }
    }

    private fun setAdapter() {
        activity?.let { activity ->
            binding.recyclerViewGridGuestLobby.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
            binding.recyclerViewGridGuestLobby.addItemDecoration(ItemOffsetDecoration(activity, R.dimen._10dp))
            adapter = GuestLobbyAdapter(activity, activity.resources.getStringArray(R.array.guest_lobby_list), this)
            binding.recyclerViewGridGuestLobby.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    override fun onGuestLobbyGridItemClickedEvent(lobbyGridItem: String?) {
        when(lobbyGridItem) {
            getString(R.string.about_us) -> {
                openBrowser(Constants.ABOUT_US_LINK)
            }
            getString(R.string.building_forward) -> {
                openBrowser(Constants.GUEST_BUILDING_FORWARD_LINK)
            }
            getString(R.string.join_our_team) -> {
                openBrowser(Constants.JOIN_OUR_TEAM_LINK)
            }
            getString(R.string.contact_us) -> {
                openBrowser(Constants.CONTACT_US_LINK)
            }

        }
    }

    override fun onSignOutClicked() {
        findNavController().navigateUp()
    }
}

interface GuestLobbyFragmentEventListener {
    fun onGuestLobbyGridItemClickedEvent(lobbyGridItem: String?)
    fun onSignOutClicked()
}

