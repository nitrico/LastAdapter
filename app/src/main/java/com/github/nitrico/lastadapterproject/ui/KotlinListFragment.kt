package com.github.nitrico.lastadapterproject.ui

import android.os.Bundle
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.BR
import com.github.nitrico.lastadapterproject.data.Data
import com.github.nitrico.lastadapterproject.R
import com.github.nitrico.lastadapterproject.data.Header
import com.github.nitrico.lastadapterproject.data.Point

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
                .onClick { if (type == R.layout.item_point) activity?.toast("onClick $position of type $type: $item") }
                .onLongClick { activity?.toast("onLongClick $position of type $type: $item") }
                .into(list)
    }

    /*
    override fun onBind(item: Any, view: View, type: Int, position: Int) {
        when (type) {
            R.layout.item_header_first -> {
                val binding = DataBindingUtil.getBinding<ItemHeaderFirstBinding>(view)
                binding.headerFirstText.tag = "firstHeader"
            }
            R.layout.item_header -> {
                val binding = DataBindingUtil.getBinding<ItemHeaderBinding>(view)
                val header = item as Header
                binding.headerText.tag = "header" + header.text
            }
            R.layout.item_point -> {
                val binding = DataBindingUtil.getBinding<ItemPointBinding>(view)
                val point = item as Point
                binding.pointX.tag = "X:" + point.x
                binding.pointY.tag = "Y:" + point.y
            }
        }
    }
    */

}
