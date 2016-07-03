package com.github.nitrico.lastadapter

import android.databinding.*
import android.os.Looper
import android.support.annotation.Keep
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference

@Keep
class LastAdapter<T : Any> private constructor(private val list: List<T>,
                                               private val variable: Int,
                                               private val map: Map<Class<*>, Int>,
                                               private val handler: LayoutHandler?,
                                               private val onBind: OnBindListener?,
                                               private val onClick: OnClickListener?,
                                               private val onLongClick: OnLongClickListener?)
: RecyclerView.Adapter<LastAdapter.ViewHolder>() {

    companion object {
        @JvmStatic fun <T : Any> with(list: List<T>, variable: Int) = Builder(list, variable)
    }

    @Keep
    class Builder<T : Any> internal constructor(private val list: List<T>, private val variable: Int) {

        private val map: MutableMap<Class<*>, Int> = mutableMapOf()
        private var handler: LayoutHandler? = null
        private var onBind: OnBindListener? = null
        private var onClick: OnClickListener? = null
        private var onLongClick: OnLongClickListener? = null

        inline fun <reified T : Any> map(@LayoutRes layout: Int) = map(T::class.java, layout)

        fun map(clazz: Class<*>, @LayoutRes layout: Int) = apply { map.put(clazz, layout) }

        inline fun layout(crossinline f: ItemPosition.() -> Int) = apply {
            layoutHandler(object : LayoutHandler {
                override fun getItemLayout(item: Any, position: Int)
                        = ItemPosition(item, position).f()
            })
        }

        fun layoutHandler(layoutHandler: LayoutHandler) = apply { handler = layoutHandler }

        inline fun onBind(crossinline f: ItemViewPosition.() -> Unit) = apply {
            onBindListener(object : OnBindListener {
                override fun onBind(item: Any, view: View, position: Int)
                        = ItemViewPosition(item, view, position).f()
            })
        }

        fun onBindListener(listener: OnBindListener) = apply { onBind = listener }

        inline fun onClick(crossinline f: ItemViewPosition.() -> Unit) = apply {
            onClickListener(object : OnClickListener {
                override fun onClick(item: Any, view: View, position: Int)
                        = ItemViewPosition(item, view, position).f()
            })
        }

        fun onClickListener(listener: OnClickListener) = apply { onClick = listener }

        inline fun onLongClick(crossinline f: ItemViewPosition.() -> Unit) = apply {
            onLongClickListener(object : OnLongClickListener {
                override fun onLongClick(item: Any, view: View, position: Int)
                        = ItemViewPosition(item, view, position).f()
            })
        }

        fun onLongClickListener(listener: OnLongClickListener) = apply { onLongClick = listener }

        fun into(recyclerView: RecyclerView) = build().apply { recyclerView.adapter = this }

        fun build() = LastAdapter(list, variable, map, handler, onBind, onClick, onLongClick)

    }


    class ItemPosition(val item: Any, val position: Int)

    class ItemViewPosition(val item: Any, val view: View, val position: Int)


    interface LayoutHandler {
        @LayoutRes fun getItemLayout(item: Any, position: Int): Int
    }

    interface OnBindListener {
        fun onBind(item: Any, view: View, position: Int)
    }

    interface OnClickListener {
        fun onClick(item: Any, view: View, position: Int)
    }

    interface OnLongClickListener {
        fun onLongClick(item: Any, view: View, position: Int)
    }


    class ViewHolder(internal val binding: ViewDataBinding, internal val variable: Int)
    : RecyclerView.ViewHolder(binding.root) {

        fun bindTo(item: Any, pos: Int, onBind: OnBindListener?, onClick: OnClickListener?,
                   onLongClick: OnLongClickListener?) {

            binding.setVariable(variable, item)
            binding.executePendingBindings()
            val view = binding.root

            if (onClick != null) view.setOnClickListener {
                onClick.onClick(item, view, pos)
            }
            if (onLongClick != null) view.setOnLongClickListener {
                onLongClick.onLongClick(item, view, pos); true
            }
            onBind?.onBind(item, view, pos)
        }

    }


    private val DATA_INVALIDATION = Any()
    private val onChange = WeakReferenceOnListChangedCallback(this)
    private var recyclerView: RecyclerView? = null
    private var inflater: LayoutInflater? = null

    override fun onCreateViewHolder(view: ViewGroup, type: Int): ViewHolder {
        if (inflater == null) inflater = LayoutInflater.from(view.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, type, view, false)
        val holder = ViewHolder(binding, variable)
        addOnRebindCallback(binding, recyclerView, holder.adapterPosition)
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.bindTo(list[pos], pos, onBind, onClick, onLongClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int, payloads: MutableList<Any>?) {
        if (isForDataBinding(payloads)) holder.binding.executePendingBindings()
        else onBindViewHolder(holder, pos)
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(pos: Int) = handler?.getItemLayout(list[pos], pos)
            ?: map[list[pos].javaClass]
            ?: throw RuntimeException("Invalid object at position $pos: ${list[pos]}")

    override fun onAttachedToRecyclerView(rv: RecyclerView?) {
        if (recyclerView == null && list is ObservableList) list.addOnListChangedCallback(onChange)
        recyclerView = rv
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView?) {
        if (recyclerView != null && list is ObservableList) list.removeOnListChangedCallback(onChange)
        recyclerView = null
    }

    private fun addOnRebindCallback(b: ViewDataBinding, rv: RecyclerView?, pos: Int) {
        b.addOnRebindCallback(object: OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding) = rv != null && rv.isComputingLayout
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


    private class WeakReferenceOnListChangedCallback<T : Any>(private val adapter: LastAdapter<T>)
    : ObservableList.OnListChangedCallback<ObservableList<T>>() {

        private val adapterRef = WeakReference<LastAdapter<T>>(adapter)

        private fun getAdapter(): LastAdapter<T> {
            if (Thread.currentThread() != Looper.getMainLooper().thread) {
                throw IllegalStateException("You cannot modify the ObservableList on a background thread")
            }
            return adapterRef.get()
        }

        override fun onChanged(list: ObservableList<T>)
                = getAdapter().notifyDataSetChanged()

        override fun onItemRangeChanged(list: ObservableList<T>, from: Int, count: Int)
                = getAdapter().notifyItemRangeChanged(from, count)

        override fun onItemRangeInserted(list: ObservableList<T>, from: Int, count: Int)
                = getAdapter().notifyItemRangeInserted(from, count)

        override fun onItemRangeRemoved(list: ObservableList<T>, from: Int, count: Int)
                = getAdapter().notifyItemRangeRemoved(from, count)

        override fun onItemRangeMoved(list: ObservableList<T>, from: Int, to: Int, count: Int)
                = with(getAdapter()) { for (i in 0..count) notifyItemMoved(from+i, to+i) }

    }

}
