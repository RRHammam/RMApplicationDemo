package com.robinsmorton.rmappandroid.util

import android.view.View
import androidx.databinding.BindingAdapter
import kotlinx.android.synthetic.main.item_loading_spinner.view.*

@BindingAdapter("app:setVisibilityAndBringToFront")
fun setVisibility(view: View, isVisible: Boolean) {
    if (isVisible ) {
        view.loading_spinner.visibility = View.VISIBLE
        view.visibility = View.VISIBLE
        view.bringToFront()
    } else {
        view.loading_spinner.visibility = View.GONE
        view.visibility = View.GONE
    }
}