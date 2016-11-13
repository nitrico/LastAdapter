package com.github.nitrico.lastadapter_sample.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.github.nitrico.lastadapter.*
import com.github.nitrico.lastadapter_sample.BR
import com.github.nitrico.lastadapter_sample.R
import com.github.nitrico.lastadapter_sample.data.*
import com.github.nitrico.lastadapter_sample.databinding.*

class KotlinListFragment : ListFragment() {

    private val TYPE_HEADER = Type<ItemHeaderBinding>(R.layout.item_header)
            .onBind { println("Bound ${binding.item} at #$position") }
            .onRecycle { println("Recycled ${binding.item} at #$position") }
            .onClick { activity.toast("Clicked #$position: ${binding.item}") }
            .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }

    private val TYPE_HEADER_FIRST = Type<ItemHeaderFirstBinding>(R.layout.item_header_first)
            .onBind { println("Bound ${binding.item} at #$position") }
            .onRecycle { println("Recycled ${binding.item} at #$position") }
            .onClick { activity.toast("Clicked #$position: ${binding.item}") }
            .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }

    private val TYPE_POINT = Type<ItemPointBinding>(R.layout.item_point)
            .onBind { println("Bound ${binding.item} at #$position") }
            .onRecycle { println("Recycled ${binding.item} at #$position") }
            .onClick { activity.toast("Clicked #$position: ${binding.item}") }
            .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }

    private val TYPE_CAR = Type<ItemCarBinding>(R.layout.item_car)
            .onBind { println("Bound ${binding.item} at #$position") }
            .onRecycle { println("Recycled ${binding.item} at #$position") }
            .onClick { activity.toast("Clicked #$position: ${binding.item}") }
            .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }

    private val TYPE_PERSON = Type<ItemPersonBinding>(R.layout.item_person)
            .onBind { println("Bound ${binding.item} at #$position") }
            .onRecycle { println("Recycled ${binding.item} at #$position") }
            .onClick { activity.toast("Clicked #$position: ${binding.item}") }
            .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val items = Data.items
        val stableIds = items == StableData.items

        //setMapAdapter(items, stableIds)
        //setMapAdapterWithListeners(items, stableIds)
        //setLayoutHandlerAdapter(items, stableIds)
        setTypeHandlerAdapter(items, stableIds)
    }

    private fun setMapAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter.with(items, BR.item, stableIds)
                .map<Person>(R.layout.item_person)
                .map<Car>(R.layout.item_car)
                .map<Header>(R.layout.item_header)
                .map<Point>(R.layout.item_point)
                .into(list)
    }

    private fun setMapAdapterWithListeners(items: List<Any>, stableIds: Boolean) {
        LastAdapter.with(items, BR.item, stableIds)
                .map<Header, ItemHeaderBinding>(R.layout.item_header)
                .map<Point>(TYPE_POINT)
                .map<Car>(Type<ItemCarBinding>(R.layout.item_car)
                        .onBind { println("Bound ${binding.item} at #$position") }
                        .onRecycle { println("Recycled ${binding.item} at #$position") }
                        .onClick { activity.toast("Clicked #$position: ${binding.item}") }
                        .onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }
                )
                .map<Person, ItemPersonBinding>(R.layout.item_person) {
                    onBind { println("Bound ${binding.item} at #$position") }
                    onRecycle { println("Recycled ${binding.item} at #$position") }
                    onClick { activity.toast("Clicked #$position: ${binding.item}") }
                    onLongClick { activity.toast("Long-clicked #$position: ${binding.item}") }
                }
                .into(list)
    }

    private fun setLayoutHandlerAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter.with(items, BR.item, stableIds).layout {
            when (item) {
                is Header -> if (position % 2 == 0) R.layout.item_header_first else R.layout.item_header
                is Person -> R.layout.item_person
                is Point -> R.layout.item_point
                is Car -> R.layout.item_car
                else -> -1
            }
        }.into(list)
    }

    private fun setTypeHandlerAdapter(items: List<Any>, stableIds: Boolean) {
        LastAdapter.with(items, BR.item, stableIds).type {
            when (item) {
                is Header -> if (position % 2 == 0) TYPE_HEADER_FIRST else TYPE_HEADER
                is Point -> TYPE_POINT
                is Person -> TYPE_PERSON
                is Car -> TYPE_CAR
                else -> null
            }
        }.into(list)
    }

    private fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

}
