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

class LastAdapter(private val list: List<Any>,
                  private val variable: Int? = null,
                  stableIds: Boolean = false) : RecyclerView.Adapter<Holder<ViewDataBinding>>() {

    constructor(list: List<Any>) : this(list, null, false)
    constructor(list: List<Any>, variable: Int) : this(list, variable, false)
    constructor(list: List<Any>, stableIds: Boolean) : this(list, null, stableIds)

    private val DATA_INVALIDATION = Any()
    private val callback = ObservableListCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null

    private val map = mutableMapOf<Class<*>, BaseType>()
    private var layoutHandler: LayoutHandler? = null
    private var typeHandler: TypeHandler? = null

    init {
        setHasStableIds(stableIds)
    }

    @JvmOverloads
    fun <T : Any> map(clazz: Class<T>, layout: Int, variable: Int? = null)
            = apply { map[clazz] = BaseType(layout, variable) }

    inline fun <reified T : Any> map(layout: Int, variable: Int? = null)
            = map(T::class.java, layout, variable)

    fun <T : Any> map(clazz: Class<T>, type: AbsType<*>)
            = apply { map[clazz] = type }

    inline fun <reified T : Any> map(type: AbsType<*>)
            = map(T::class.java, type)

    inline fun <reified T : Any, B : ViewDataBinding> map(layout: Int,
                                                          variable: Int? = null,
                                                          noinline f: (Type<B>.() -> Unit)? = null)
            = map(T::class.java, Type<B>(layout, variable).apply { f?.invoke(this) })

    fun handler(handler: Handler) = apply {
        when (handler) {
            is LayoutHandler -> {
                if (variable == null) {
                    throw IllegalStateException("No variable specified in LastAdapter constructor")
                }
                layoutHandler = handler
            }
            is TypeHandler -> typeHandler = handler
        }
    }

    inline fun layout(crossinline f: (Any, Int) -> Int) = handler(object : LayoutHandler {
        override fun getItemLayout(item: Any, position: Int) = f(item, position)
    })

    inline fun type(crossinline f: (Any, Int) -> AbsType<*>?) = handler(object : TypeHandler {
        override fun getItemType(item: Any, position: Int) = f(item, position)
    })

    fun into(recyclerView: RecyclerView) = apply { recyclerView.adapter = this }



    override fun onCreateViewHolder(view: ViewGroup, viewType: Int): Holder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, view, false)
        val holder = Holder(binding)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding) = recyclerView?.isComputingLayout ?: false
            override fun onCanceled(binding: ViewDataBinding) {
                if (recyclerView?.isComputingLayout ?: true) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
        })
        return holder
    }

    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int) {
        val type = getType(position)!!
        holder.binding.setVariable(getVariable(type), list[position])
        holder.binding.executePendingBindings()
        @Suppress("UNCHECKED_CAST")
        if (type is AbsType<*>) {
            if (!holder.created) {
                notifyCreate(holder, type as AbsType<ViewDataBinding>)
            }
            notifyBind(holder, type as AbsType<ViewDataBinding>)
        }
    }

    override fun onBindViewHolder(holder: Holder<ViewDataBinding>, position: Int, payloads: List<Any>) {
        if (isForDataBinding(payloads)) {
            holder.binding.executePendingBindings()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onViewRecycled(holder: Holder<ViewDataBinding>) {
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            val type = getType(position)!!
            if (type is AbsType<*>) {
                @Suppress("UNCHECKED_CAST")
                notifyRecycle(holder, type as AbsType<ViewDataBinding>)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        if (hasStableIds()) {
            val item = list[position]
            if (item is StableId) {
                return item.stableId
            } else {
                throw IllegalStateException("${item.javaClass.simpleName} must implement StableId interface.")
            }
        } else {
            return super.getItemId(position)
        }
    }

    override fun getItemCount() = list.size

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

    override fun getItemViewType(position: Int)
            = layoutHandler?.getItemLayout(list[position], position)
            ?: typeHandler?.getItemType(list[position], position)?.layout
            ?: getType(position)?.layout
            ?: throw RuntimeException("Invalid object at position $position: ${list[position].javaClass}")

    private fun getType(position: Int)
            = typeHandler?.getItemType(list[position], position)
            ?: map[list[position].javaClass]

    private fun getVariable(type: BaseType)
            = type.variable
            ?: variable
            ?: throw IllegalStateException("No variable specified for type ${type.javaClass.simpleName}")

    private fun isForDataBinding(payloads: List<Any>): Boolean {
        if (payloads.isEmpty()) {
            return false
        }
        payloads.forEach {
            if (it != DATA_INVALIDATION) {
                return false
            }
        }
        return true
    }

    private fun notifyCreate(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> {
                setClickListeners(holder, type)
                type.onCreate?.invoke(holder)
            }
            is ItemType -> type.onCreate(holder)
        }
        holder.created = true
    }

    private fun notifyBind(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> type.onBind?.invoke(holder)
            is ItemType -> type.onBind(holder)
        }
    }

    private fun notifyRecycle(holder: Holder<ViewDataBinding>, type: AbsType<ViewDataBinding>) {
        when (type) {
            is Type -> type.onRecycle?.invoke(holder)
            is ItemType -> type.onRecycle(holder)
        }
    }

    private fun setClickListeners(holder: Holder<ViewDataBinding>, type: Type<ViewDataBinding>) {
        val onClick = type.onClick
        if (onClick != null) {
            holder.itemView.setOnClickListener {
                onClick(holder)
            }
        }
        val onLongClick = type.onLongClick
        if (onLongClick != null) {
            holder.itemView.setOnLongClickListener {
                onLongClick(holder)
                true
            }
        }
    }

}
