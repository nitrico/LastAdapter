package com.github.nitrico.lastadapterproject

import android.os.Bundle
import android.view.View
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point
import kotlinx.android.synthetic.main.fragment_list.*

class KotlinListFragment : ListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LastAdapter.with(Data.items, BR.item)
                .onBind { println("onBind #$position: $item") }
                .onClick { activity.toast("onClick #$position: $item") }
                .onLongClick { activity.toast("onLongClick #$position: $item") }
                //.map<Header>(R.layout.item_header)
                //.map<Point>(R.layout.item_point)
                .layout { when (item) {
                    is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
                    is Point -> R.layout.item_point
                    else -> 0
                }
                }.into(list)
    }

}
