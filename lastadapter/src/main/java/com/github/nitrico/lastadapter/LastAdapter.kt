/*
 * Copyright (C) 2016 Miguel Ángel Moreno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nitrico.lastadapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableList
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class LastAdapter<B : ViewDataBinding> private constructor(private val list: List<Any>,
                                                           private val variable: Int,
                                                           stableIds: Boolean = false) : RecyclerView.Adapter<ViewHolder<B>>() {

    private val DATA_INVALIDATION = Any()
    private val callback = ObservableListCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null

    private val map = mutableMapOf<Class<*>, SimpleType>()
    private var layoutHandler: LayoutHandler? = null
    private var typeHandler: TypeHandler? = null

    init {
        setHasStableIds(stableIds)
    }

    companion object {
        @JvmStatic @JvmOverloads
        fun with(list: List<Any>, variable: Int, stableIds: Boolean = false) = LastAdapter<ViewDataBinding>(list, variable, stableIds)
    }

    fun <T : Any> map(clazz: Class<T>, layout: Int) = apply { map[clazz] = SimpleType(layout) }

    inline fun <reified T : Any> map(layout: Int) = map(T::class.java, layout)

    fun <T : Any> map(clazz: Class<T>, type: BaseType<*>) = apply { map[clazz] = type }

    inline fun <reified T : Any> map(type: BaseType<*>) = map(T::class.java, type)

    inline fun <reified T : Any, B : ViewDataBinding> map(layout: Int, noinline f: Type<B>.() -> Unit = { })
            = map(T::class.java, Type<B>(layout).apply { f() })

    fun handler(handler: Handler) = apply {
        when (handler) {
            is LayoutHandler -> layoutHandler = handler
            is TypeHandler -> typeHandler = handler
        }
    }

    inline fun layout(crossinline f: (Any, Int) -> Int) = handler(object : LayoutHandler {
        override fun getItemLayout(item: Any, position: Int) = f(item, position)
    })

    inline fun type(crossinline f: (Any, Int) -> BaseType<*>?) = handler(object : TypeHandler {
        override fun getItemType(item: Any, position: Int) = f(item, position)
    })

    fun into(recyclerView: RecyclerView) = apply { recyclerView.adapter = this }



    override fun onCreateViewHolder(view: ViewGroup, type: Int): ViewHolder<B> {
        val binding = DataBindingUtil.inflate<B>(inflater, type, view, false)
        val holder = ViewHolder<B>(binding)
        val position = holder.adapterPosition
        binding.addOnRebindCallback(object : OnRebindCallback<B>() {
            //override fun onPreBind(binding: B) = recyclerView!!.isComputingLayout
            override fun onCanceled(binding: B) {
                if (!recyclerView!!.isComputingLayout && position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        val type = getType(position)!!
        if (type is BaseType<*>) {
            @Suppress("UNCHECKED_CAST")
            holder.bind(variable, list[position], type as BaseType<B>)
        }
        else holder.bind(variable, list[position])
    }

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int, payloads: MutableList<Any>) {
        if (isForDataBinding(payloads)) holder.binding.executePendingBindings()
        else onBindViewHolder(holder, position)
    }

    private fun isForDataBinding(payloads: List<Any>): Boolean {
        if (payloads.isEmpty()) return false
        payloads.forEach { if (it == DATA_INVALIDATION) return false }
        return true
    }

    override fun onViewRecycled(holder: ViewHolder<B>) {
        val position = holder.adapterPosition
        if (position > 0 && position < list.size) { // ?
            val type = getType(position)!!
            if (type is BaseType<*>) {
                @Suppress("UNCHECKED_CAST")
                holder.recycle(type as BaseType<B>)
            }
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = layoutHandler?.getItemLayout(list[position], position)
            ?: typeHandler?.getItemType(list[position], position)?.layout
            ?: getType(position)?.layout
            ?: throw RuntimeException("Invalid object at position $position: ${list[position].javaClass}")

    private fun getType(position: Int) = typeHandler?.getItemType(list[position], position)
            ?: map[list[position].javaClass]

    override fun getItemId(position: Int): Long {
        if (hasStableIds()) {
            val item = list[position]
            if (item is StableId) return item.stableId
            throw RuntimeException("${item.javaClass.simpleName} must implement StableId interface.")
        }
        return super.getItemId(position)
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        if (recyclerView == null && list is ObservableList) {
            list.addOnListChangedCallback(callback)
        }
        recyclerView = rv
        inflater = LayoutInflater.from(rv.context)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        if (recyclerView != null && list is ObservableList) {
            list.removeOnListChangedCallback(callback)
        }
        recyclerView = null
    }

}
