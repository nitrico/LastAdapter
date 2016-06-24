package com.github.nitrico.lastadapterproject

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point
import kotlinx.android.synthetic.main.fragment_list.*

class KotlinListFragment : ListFragment(), LastAdapter.OnBindListener {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        LastAdapter.with(Data.items, BR.item)
                .map<Header>(R.layout.item_header)
                .map<Point>(R.layout.item_point)
                .onBindListener(this)
                .into(list)
    }

    override fun onBind(item: Any, view: View, position: Int) {
        if (item is Header) (view as TextView).text = "$position"
        println("onBind position $position: $item")
    }

}
