package com.github.nitrico.lastadapter

import android.databinding.*
import android.support.annotation.Keep
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

@Keep
class LastAdapter<T : Any> private constructor(private val list: List<T>,
                                               private val variable: Int,
                                               private val map: Map<out Class<*>, Int>)
: RecyclerView.Adapter<LastAdapter.ViewHolder<T>>() {

    private constructor(builder: Builder<T>) : this(builder.list, builder.variable, builder.map)

    companion object {
        @JvmStatic fun <T : Any> with(list: List<T>, variable: Int) = Builder(list, variable)
    }

    @Keep
    class Builder<T : Any> internal constructor(val list: List<T>, val variable: Int) {
        internal val map: MutableMap<Class<*>, Int> = mutableMapOf()
        fun map(clazz: Class<*>, layout: Int) = apply { map.put(clazz, layout) }
        fun build() = LastAdapter(this)
        fun into(recyclerView: RecyclerView): LastAdapter<T> {
            val adapter = build()
            recyclerView.adapter = adapter
            return adapter
        }
    }


    class ViewHolder<T>(val binding: ViewDataBinding, val variable: Int) : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(item: T) {
            binding.setVariable(variable, item)
            binding.executePendingBindings()
        }
    }


    private val DATA_INVALIDATION = Any()
    private val callback = WeakReferenceOnListChangedCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView == null && list is ObservableList) list.addOnListChangedCallback(callback)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView != null && list is ObservableList) list.removeOnListChangedCallback(callback)
        this.recyclerView = null
    }

    override fun onCreateViewHolder(view: ViewGroup, type: Int): ViewHolder<T> {
        if (inflater == null) inflater = LayoutInflater.from(view.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, type, view, false)
        val holder = ViewHolder<T>(binding, variable)
        addOnRebindCallback(binding, recyclerView, holder.adapterPosition)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, i: Int) = holder.bindTo(list[i])

    override fun onBindViewHolder(holder: ViewHolder<T>, i: Int, payloads: MutableList<Any>?) {
        if (isForDataBinding(payloads)) holder.binding.executePendingBindings()
        else onBindViewHolder(holder, i)
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(i: Int) = map[list[i].javaClass]
            ?: throw RuntimeException("Invalid object at position: $i: ${list[i]}")

    private fun addOnRebindCallback(b: ViewDataBinding, rv: RecyclerView?, pos: Int) {
        b.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding) = rv != null && rv.isComputingLayout
            override fun onCanceled(binding: ViewDataBinding) {
                if (rv == null || rv.isComputingLayout) return
                if (pos != RecyclerView.NO_POSITION) notifyItemChanged(pos, DATA_INVALIDATION)
            }
        })
    }

    private fun isForDataBinding(payloads: List<Any>?): Boolean {
        if (payloads == null || payloads.isEmpty()) return false
        payloads.forEach { if (it == DATA_INVALIDATION) return false }
        return true
    }

}
