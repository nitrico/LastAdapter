package com.github.nitrico.lastadapterproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Point
import kotlinx.android.synthetic.main.fragment_list.*

class KotlinListFragment : ListFragment(),
                           LastAdapter.OnBindListener,
                           LastAdapter.OnClickListener,
                           LastAdapter.OnLongClickListener {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        LastAdapter.with(Data.items, BR.item)
                .map<Header>(R.layout.item_header)
                .map<Point>(R.layout.item_point)
                .onBindListener(this)
                .onClickListener(this)
                .onLongClickListener(this)
                .into(list)
    }

    override fun onBind(item: Any, view: View, position: Int) {
        //if (item is Header) (view as TextView).text = "$position"
        println("onBind position $position: $item")
    }

    override fun onClick(item: Any, view: View, position: Int) {
        Toast.makeText(activity, "Clicked $position $item", Toast.LENGTH_SHORT).show()
    }

    override fun onLongClick(item: Any, view: View, position: Int) {
        Toast.makeText(activity, "Long clicked $position $item", Toast.LENGTH_SHORT).show()
    }

}
