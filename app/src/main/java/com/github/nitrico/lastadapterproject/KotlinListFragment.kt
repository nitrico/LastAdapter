package com.github.nitrico.lastadapterproject

import android.os.Bundle
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point

class KotlinListFragment : ListFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LastAdapter.with(Data.items, BR.item)
                //.map<Header>(R.layout.item_header)
                //.map<Point>(R.layout.item_point)
                .layout {
                    when (item) {
                        is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
                        is Point -> R.layout.item_point
                        else -> 0
                    }
                }
                .onBind { println("binded position $position of type $type: $item") }
                .onClick { if (type == R.layout.item_point) activity.toast("onClick $position of type $type: $item") }
                .onLongClick { activity.toast("onLongClick $position of type $type: $item") }
                .into(list)
    }

}
