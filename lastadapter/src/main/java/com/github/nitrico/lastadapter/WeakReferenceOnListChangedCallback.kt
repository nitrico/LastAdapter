package com.github.nitrico.lastadapter

import android.databinding.ObservableList
import android.os.Looper
import java.lang.ref.WeakReference

internal class WeakReferenceOnListChangedCallback<T : Any>(private val adapter: LastAdapter<T>)
: ObservableList.OnListChangedCallback<ObservableList<T>>() {

    private val adapterRef = WeakReference<LastAdapter<T>>(adapter)

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

    private fun getAdapter(): LastAdapter<T> {
        if (Thread.currentThread() != Looper.getMainLooper().thread) {
            throw IllegalStateException("You cannot modify the ObservableList on a background thread")
        }
        return adapterRef.get()
    }

}
