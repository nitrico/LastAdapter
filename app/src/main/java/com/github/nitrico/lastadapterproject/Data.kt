package com.github.nitrico.lastadapterproject

import android.databinding.ObservableArrayList
import com.github.nitrico.lastadapterproject.item.Header
import com.github.nitrico.lastadapterproject.item.Page
import com.github.nitrico.lastadapterproject.item.Point

object Data {

    val items = ObservableArrayList<Any>()
    val pages = ObservableArrayList<Any>()

    init {
        with(items) {
            add(Header("Header 1"))
            add(Point(1, 1))
            add(Header("Header 2"))
            add(Point(2, 1))
            add(Point(2, 2))
            add(Header("Header 3"))
            add(Point(3, 1))
            add(Point(3, 2))
            add(Point(3, 3))
            add(Header("Header 4"))
            add(Point(4, 1))
            add(Point(4, 2))
            add(Point(4, 3))
            add(Point(4, 4))
            add(Header("Header 5"))
            add(Point(5, 1))
            add(Point(5, 2))
            add(Point(5, 3))
            add(Point(5, 4))
            add(Point(5, 5))
        }
        with(pages) {
            add(Page("1"))
            add(Page("2"))
            add(Page("3"))
            add(Page("4"))
        }
    }

}
