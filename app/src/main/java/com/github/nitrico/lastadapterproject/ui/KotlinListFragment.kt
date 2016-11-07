package com.github.nitrico.lastadapterproject.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.BR
import com.github.nitrico.lastadapterproject.R
import com.github.nitrico.lastadapterproject.data.Data
import com.github.nitrico.lastadapterproject.data.Header
import com.github.nitrico.lastadapterproject.data.Point
import com.github.nitrico.lastadapterproject.databinding.ItemHeaderBinding
import com.github.nitrico.lastadapterproject.databinding.ItemHeaderFirstBinding
import com.github.nitrico.lastadapterproject.databinding.ItemPointBinding

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
                .onBind { println("bound position $position of type $type: $item") }
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

    private fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}
