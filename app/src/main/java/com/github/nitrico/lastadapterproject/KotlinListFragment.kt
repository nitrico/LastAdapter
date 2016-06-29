package com.github.nitrico.lastadapterproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point
import kotlinx.android.synthetic.main.fragment_list.*

class KotlinListFragment : ListFragment(),
                           LastAdapter.LayoutHandler,
                           LastAdapter.OnBindListener,
                           LastAdapter.OnClickListener,
                           LastAdapter.OnLongClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LastAdapter.with(Data.items, BR.item)
                .layoutHandler(this)
                .onBindListener(this)
                .onClickListener(this)
                .onLongClickListener(this)
                .into(list)
    }

    override fun getItemLayout(item: Any, index: Int) = when (item) {
        is Header -> if (index == 0) R.layout.item_header_first else R.layout.item_header
        is Point -> R.layout.item_point
        else -> 0
    }

    override fun onBind(item: Any, view: View, position: Int) {
        println("onBind position $position: $item")
    }

    override fun onClick(item: Any, view: View, position: Int) {
        Toast.makeText(activity, "Clicked $position $item", Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(item: Any, view: View, position: Int) {
        Toast.makeText(activity, "Long clicked $position $item", Toast.LENGTH_SHORT).show()
    }

}
