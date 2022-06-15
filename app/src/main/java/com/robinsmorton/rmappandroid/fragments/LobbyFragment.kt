package com.robinsmorton.rmappandroid.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.robinsmorton.rmappandroid.adapter.LobbyAdapter
import com.robinsmorton.rmappandroid.constants.Constants
import com.robinsmorton.rmappandroid.databinding.FragmentLobbyBinding
import com.robinsmorton.rmappandroid.model.Item
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel.Companion.cmd_hide_loading_sign
import com.robinsmorton.rmappandroid.viewmodel.LobbyViewModel.Companion.cmd_show_loading_sign
import kotlinx.android.synthetic.main.fragment_lobby.*
import kotlinx.android.synthetic.main.item_loading_spinner.view.*


class LobbyFragment : BaseFragment(), LobbyFragmentEventListener {
    private lateinit var binding: FragmentLobbyBinding
    private lateinit var viewModel: LobbyViewModel
    private var adapter: LobbyAdapter? = null

    private val TAG = "LobbyFragment"

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
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lobby, container, false)
        viewModel = ViewModelProvider(this).get(LobbyViewModel::class.java)
        return with(binding) {
            root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToRmAppListLiveData()
        subscribeToEventCommands()
        viewModel.getRmAppList()
        (activity as MainActivity).getBottomNavView()?.visibility = View.VISIBLE
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

    private fun subscribeToRmAppListLiveData() {
        viewModel.rmAppLobbyListLiveData.observe(viewLifecycleOwner, {
            activity?.let { activity ->
                binding.recyclerViewGridLobby.layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
                binding.recyclerViewGridLobby.addItemDecoration(ItemOffsetDecoration(activity, R.dimen._10dp))
            }
            adapter = this.context?.let { it1 -> LobbyAdapter(it1, it, this) }
            itemTouchHelper.attachToRecyclerView(binding.recyclerViewGridLobby)
            binding.recyclerViewGridLobby.adapter = adapter
            adapter?.notifyDataSetChanged()
        })
    }

    override fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?) {
        when(lobbyGridItem?.fields?.Title?.trim()) {
            getString(R.string.corporate_directory) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_corporateDirectoryFragment)
            }
            getString(R.string.project_numbers) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_jobRequestFragment)
            }
            getString(R.string.estimate_numbers) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_estimateNumbersFragment)
            }
            getString(R.string.sustainability) -> {
                openBrowser(Constants.SUSTAINABILITY_LINK)
            }
            getString(R.string.waste_management) -> {
                openBrowser(Constants.WASTE_MANAGEMENT_LINK)
            }
            getString(R.string.building_forward) -> {
                openBrowser(Constants.BUILDING_FORWARD_LINK)
            }
            getString(R.string.time_entry_paystubs) -> {
                openBrowser(Constants.HR_INFORMATION_LINK)
            }
            getString(R.string.safety_information) -> {
                openBrowser(Constants.SAFETY_INFORMATION_LINK)
            }
            getString(R.string.applications) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_applicationsFragment)
            }
            getString(R.string.policies_and_procedures) -> {
                findNavController().navigate(R.id.action_lobbyFragment_to_policiesAndProceduresFragment)
            }
            getString(R.string.it_support) -> {
                openBrowser(Constants.IT_SUPPORT_LINK)
            }
            getString(R.string.rm_web) -> {
                openBrowser(Constants.TRAINING_EXCELLENCE_LINK)
            }
        }
    }

    private fun openBrowser(link : String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        this.activity?.let { activity -> browserIntent.resolveActivity(activity.packageManager) }
            ?.let { startActivity(browserIntent) }
    }

    private fun subscribeToEventCommands() {
        viewModel.eventCommand.observe(viewLifecycleOwner,{
            when(it) {
                cmd_show_loading_sign -> showProgressBar()

                cmd_hide_loading_sign -> hideProgressBar()
            }
        })
    }

    private fun showProgressBar() {
        binding.progressBar.bringToFront()
        binding.progressBar.loading_spinner.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.rmAppLobbyListLiveData.removeObservers(viewLifecycleOwner)
    }
}

interface LobbyFragmentEventListener {
    fun onLobbyGridItemClickedEvent(lobbyGridItem: Item?)
}

class ItemOffsetDecoration(private val mItemOffset: Int) : ItemDecoration() {
    constructor(
        @NonNull context: Context,
        @DimenRes itemOffsetId: Int
    ) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {
    }

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect[mItemOffset, mItemOffset, mItemOffset] = mItemOffset
    }
}