package com.github.nitrico.lastadapter_sample.data

import com.github.nitrico.lastadapter.StableId

class Person(val id: Long, val name: String, val surname: String) : StableId {

    override val stableId = id

}
