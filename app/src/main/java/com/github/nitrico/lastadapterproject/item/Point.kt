package com.github.nitrico.lastadapterproject.item

import android.view.View
import android.widget.Toast

class Point(val x: Int, val y: Int) {

    fun onItemClick(v: View) {
        Toast.makeText(v.context, "Item clicked: $x $y", Toast.LENGTH_SHORT).show()
    }

    fun onItemLongClick(v: View): Boolean {
        Toast.makeText(v.context, "Item long clicked: $x $y", Toast.LENGTH_SHORT).show()
        return true
    }

}
