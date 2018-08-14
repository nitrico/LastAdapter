package com.github.nitrico.lastadapter_sample.data

import androidx.databinding.ObservableArrayList

object Data {

    val items = ObservableArrayList<Any>().apply {
        add(Header("Header 1"))
        add(Point(1, 1))
        add(Header("Header 2"))
        add(Point(2, 1))
        add(Point(2, 2))
        add(Header("Header 3"))
        add(Point(3, 1))
        add(Point(3, 2))
        add(Car(1899393, "911 Carrera"))
        add(Point(3, 3))
        add(Header("Header 4"))
        add(Point(4, 1))
        add(Point(4, 2))
        add(Point(4, 3))
        add(Person(99098, "Miguel Ángel", "Moreno"))
        add(Point(4, 4))
        add(Header("Header 5"))
        add(Point(5, 1))
        add(Point(5, 2))
        add(Point(5, 3))
        add(Point(5, 4))
        add(Point(5, 5))
        add(Header("Header 6"))
        add(Point(6, 1))
        add(Point(6, 2))
        add(Point(6, 3))
        add(Point(6, 4))
        add(Point(6, 5))
        add(Point(6, 6))
        add(Header("Header 7"))
        add(Point(7, 1))
        add(Point(7, 2))
        add(Point(7, 3))
        add(Point(7, 4))
        add(Point(7, 5))
        add(Point(7, 6))
        add(Point(7, 7))
    }

}
