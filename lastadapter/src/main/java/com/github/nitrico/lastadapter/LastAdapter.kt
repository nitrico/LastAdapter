package com.github.nitrico.lastadapter

import android.databinding.*
import android.support.annotation.Keep
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

@Keep
class LastAdapter<T : Any>(
        private val list: List<T>,
        private val variable: Int,
        private val map: Map<out Class<*>, Int>)
: RecyclerView.Adapter<LastAdapter.ViewHolder<T>>() {


    class ViewHolder<T>(val binding: ViewDataBinding, val variable: Int)
    : RecyclerView.ViewHolder(binding.root) {
        fun bindTo(item: T) {
            binding.setVariable(variable, item)
            binding.executePendingBindings()
        }
    }


    private val DATA_INVALIDATION = Any()
    private var recyclerView: RecyclerView? = null
    private val callback = WeakReferenceOnListChangedCallback(this)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView == null && list is ObservableList) list.addOnListChangedCallback(callback)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView != null && list is ObservableList) list.removeOnListChangedCallback(callback)
        this.recyclerView = null
    }

    override fun onCreateViewHolder(v: ViewGroup, type: Int): ViewHolder<T> {
        val binding = v.bind<ViewDataBinding>(type)
        val holder = ViewHolder<T>(binding, variable)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding?): Boolean {
                return recyclerView != null && recyclerView?.isComputingLayout ?: true
            }
            override fun onCanceled(binding: ViewDataBinding?) {
                if (recyclerView == null || recyclerView?.isComputingLayout ?: true) return
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) notifyItemChanged(position, DATA_INVALIDATION)
            }
        })
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

    private fun isForDataBinding(payloads: List<Any>?): Boolean {
        if (payloads == null || payloads.size == 0) return false
        payloads.forEach { if (it == DATA_INVALIDATION) return false }
        return true;
    }



    private constructor(builder: Builder<T>) : this(builder.list, builder.variable, builder.map)

    companion object {
        fun <T : Any> with(list: List<T>, variable: Int) = Builder(list, variable)
    }

    @Keep
    class Builder<T : Any> {

        internal val list: List<T>
        internal val variable: Int
        internal val map: MutableMap<Class<*>, Int> = mutableMapOf()

        internal constructor(list: List<T>, variable: Int) {
            this.list = list
            this.variable = variable
        }

        fun with(list: List<T>, variable: Int) = Builder(list, variable)

        fun map(clazz: Class<*>, layout: Int): Builder<T> {
            map.put(clazz, layout)
            return this
        }

        fun map(pair: Pair<Class<*>, Int>) = map(pair.first, pair.second)

        fun into(recyclerView: RecyclerView): LastAdapter<T> {
            val adapter = build()
            recyclerView.adapter = adapter
            return adapter
        }

        fun build() = LastAdapter(this)
    }



    private fun <T : ViewDataBinding> ViewGroup.bind(@LayoutRes layout: Int, attach: Boolean = false)
            = DataBindingUtil.inflate<T>(LayoutInflater.from(context), layout, this, attach)

}
