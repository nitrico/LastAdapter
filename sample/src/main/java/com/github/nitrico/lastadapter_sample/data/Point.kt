package com.github.nitrico.lastadapter_sample.data

import android.view.View
import android.widget.Toast

class Point(val x: Int, val y: Int) {

    fun onItemClick(v: View) {
        Toast.makeText(v.context, "Click on Point ($x,$y)", Toast.LENGTH_SHORT).show()
    }

    fun onItemLongClick(v: View): Boolean {
        Toast.makeText(v.context, "Long click on Point ($x,$y)", Toast.LENGTH_SHORT).show()
        return true
    }

}
