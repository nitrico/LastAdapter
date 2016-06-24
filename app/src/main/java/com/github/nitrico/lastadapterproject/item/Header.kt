package com.github.nitrico.lastadapterproject.item

import android.view.View
import android.widget.Toast

class Header(val text: String) {

    fun onItemClick(v: View) {
        Toast.makeText(v.context, "Header clicked: $text", Toast.LENGTH_SHORT).show()
    }

    fun onItemLongClick(v: View): Boolean {
        Toast.makeText(v.context, "Header long clicked: $text", Toast.LENGTH_SHORT).show()
        return true
    }

}
