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
import android.view.View
import android.view.ViewGroup

class LastAdapter private constructor(private val list: List<Any>,
                                      private val variable: Int,
                                      private val map: Map<Class<*>, Int>,
                                      private val layoutHandler: LayoutHandler?,
                                      private val onBindListener: OnBindListener?,
                                      private val onClickListener: OnClickListener?,
                                      private val onLongClickListener: OnLongClickListener?)
: RecyclerView.Adapter<LastAdapter.ViewHolder>() {

    companion object {
        @JvmStatic fun with(list: List<Any>, variable: Int) = Builder(list, variable)
    }

    class Builder internal constructor(private val list: List<Any>, private val variable: Int) {

        private val map = mutableMapOf<Class<*>, Int>()
        private var handler: LayoutHandler? = null
        private var onBind: OnBindListener? = null
        private var onClick: OnClickListener? = null
        private var onLongClick: OnLongClickListener? = null

        fun map(clazz: Class<*>, layout: Int) = apply { map.put(clazz, layout) }

        inline fun <reified T: Any> map(layout: Int) = map(T::class.java, layout)

        fun layoutHandler(layoutHandler: LayoutHandler) = apply { handler = layoutHandler }

        inline fun layout(crossinline f: ItemPosition.() -> Int) = layoutHandler(object: LayoutHandler {
            override fun getItemLayout(item: Any, position: Int)
                    = ItemPosition(item, position).f()
        })

        fun onBindListener(listener: OnBindListener) = apply { onBind = listener }

        inline fun onBind(crossinline f: ItemViewTypePosition.() -> Unit) = onBindListener(object: OnBindListener {
            override fun onBind(item: Any, view: View, type: Int, position: Int)
                    = ItemViewTypePosition(item, view, type, position).f()
        })

        fun onClickListener(listener: OnClickListener) = apply { onClick = listener }

        inline fun onClick(crossinline f: ItemViewTypePosition.() -> Unit) = onClickListener(object: OnClickListener {
            override fun onClick(item: Any, view: View, type: Int, position: Int)
                    = ItemViewTypePosition(item, view, type, position).f()
        })

        fun onLongClickListener(listener: OnLongClickListener) = apply { onLongClick = listener }

        inline fun onLongClick(crossinline f: ItemViewTypePosition.() -> Unit) = onLongClickListener(object: OnLongClickListener {
            override fun onLongClick(item: Any, view: View, type: Int, position: Int)
                    = ItemViewTypePosition(item, view, type, position).f()
        })

        fun into(recyclerView: RecyclerView) = build().apply { recyclerView.adapter = this }

        fun build() = LastAdapter(list, variable, map, handler, onBind, onClick, onLongClick)

    }


    inner class ViewHolder(internal val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: Any) {
            binding.setVariable(variable, item)
            binding.executePendingBindings()

            if (onClickListener != null) itemView.setOnClickListener {
                onClickListener.onClick(item, itemView, itemViewType, adapterPosition)
            }
            if (onLongClickListener != null) itemView.setOnLongClickListener {
                onLongClickListener.onLongClick(item, itemView, itemViewType, adapterPosition)
                true
            }
            onBindListener?.onBind(item, itemView, itemViewType, adapterPosition)
        }

    }


    private val DATA_INVALIDATION = Any()
    private val onChange = WeakReferenceOnListChangedCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(view: ViewGroup, type: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, type, view, false)
        val holder = ViewHolder(binding)
        addOnRebindCallback(binding, recyclerView, holder.adapterPosition)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindTo(list[position])

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>?) {
        if (isForDataBinding(payloads)) holder.binding.executePendingBindings()
        else onBindViewHolder(holder, position)
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = layoutHandler?.getItemLayout(list[position], position)
            ?: map[list[position].javaClass]
            ?: throw RuntimeException("Invalid object at position $position: ${list[position]}")

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        if (recyclerView == null && list is ObservableList) list.addOnListChangedCallback(onChange)
        recyclerView = rv
        inflater = LayoutInflater.from(rv.context)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        if (recyclerView != null && list is ObservableList) list.removeOnListChangedCallback(onChange)
        recyclerView = null
    }

    private fun addOnRebindCallback(b: ViewDataBinding, rv: RecyclerView?, pos: Int) {
        b.addOnRebindCallback(object: OnRebindCallback<ViewDataBinding>() {
            //override fun onPreBind(binding: ViewDataBinding) = rv != null && rv.isComputingLayout
            override fun onCanceled(binding: ViewDataBinding) {
                if (rv == null || rv.isComputingLayout) return
                if (pos != RecyclerView.NO_POSITION) notifyItemChanged(pos, DATA_INVALIDATION)
            }
        })
    }

    private fun isForDataBinding(payloads: List<Any>?): Boolean {
        if (payloads == null || payloads.size == 0) return false
        payloads.forEach { if (it == DATA_INVALIDATION) return false }
        return true
    }


    interface LayoutHandler {
        fun getItemLayout(item: Any, position: Int): Int
    }

    interface OnBindListener {
        fun onBind(item: Any, view: View, type: Int, position: Int)
    }

    interface OnClickListener {
        fun onClick(item: Any, view: View, type: Int, position: Int)
    }

    interface OnLongClickListener {
        fun onLongClick(item: Any, view: View, type: Int, position: Int)
    }


    class ItemPosition(val item: Any, val position: Int)

    class ItemViewTypePosition(val item: Any, val view: View, val type: Int, val position: Int)

}
