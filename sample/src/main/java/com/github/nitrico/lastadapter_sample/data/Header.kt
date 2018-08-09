package com.github.nitrico.lastadapter_sample.data

import android.view.View
import android.widget.Toast

class Header(val text: String) {

    fun onItemClick(v: View) {
        Toast.makeText(v.context, "Click on Header $text", Toast.LENGTH_SHORT).show()
    }

    fun onItemLongClick(v: View): Boolean {
        Toast.makeText(v.context, "Long click on Header $text", Toast.LENGTH_SHORT).show()
        return true
    }

}
