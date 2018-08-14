package com.github.nitrico.lastadapter_sample.data

import androidx.databinding.ObservableArrayList

object StableData {

    val items = ObservableArrayList<Any>().apply {
        add(Car(1899393, "911 Carrera"))
        add(Car(392840, "911 Carrera"))
        add(Car(3928304, "911 Carrera"))
        //add(Header("Header"))
        add(Car(329, "911 Carrera"))
        add(Car(95084, "911 Carrera"))
        add(Car(466695, "911 Carrera"))
        add(Car(908456, "911 Carrera"))
        add(Car(49308, "911 Carrera"))
        add(Person(10001, "Miguel Ángel", "Moreno"))
        add(Person(10002, "Miguel Ángel", "Moreno"))
        add(Person(10003, "Miguel Ángel", "Moreno"))
        add(Person(10004, "Miguel Ángel", "Moreno"))
        add(Person(10005, "Miguel Ángel", "Moreno"))
        add(Person(10006, "Miguel Ángel", "Moreno"))
    }

}
