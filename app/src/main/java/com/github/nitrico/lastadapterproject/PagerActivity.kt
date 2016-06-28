package com.github.nitrico.lastadapterproject

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.nitrico.lastadapterproject.item.Header
import kotlinx.android.synthetic.main.activity_main.*

/*
class PagerActivity : AppCompatActivity(),
        LastPagerAdapter.ItemTypeHandler,
        LastPagerAdapter.OnBindListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LastPagerAdapter.with(Data.items, BR.item)
                .itemTypeHandler(this)
                .onBindListener(this)
                .into(pager)
    }

    override fun onBind(item: Any, view: View, position: Int) {
        println("onBind $position: $item")
    }

    override fun getItemLayout(position: Int, item: Any)
            = if (item is Header) R.layout.item_header else R.layout.item_point

}
*/
