package com.github.nitrico.lastadapter

import android.databinding.ObservableList
import android.os.Looper
import java.lang.ref.WeakReference

class WeakReferenceOnListChangedCallback<T : Any>(private val adapter: LastAdapter<T>)
: ObservableList.OnListChangedCallback<ObservableList<T>>() {

    private val adapterRef = WeakReference<LastAdapter<T>>(adapter)

    override fun onChanged(list: ObservableList<T>?) {
        val adapter = adapterRef.get() ?: return
        ensureChangeOnMainThread()
        adapter.notifyDataSetChanged()
    }

    override fun onItemRangeChanged(list: ObservableList<T>, from: Int, count: Int) {
        val adapter = adapterRef.get() ?: return
        ensureChangeOnMainThread()
        adapter.notifyItemRangeChanged(from, count)
    }

    override fun onItemRangeMoved(list: ObservableList<T>, from: Int, to: Int, count: Int) {
        val adapter = adapterRef.get() ?: return
        ensureChangeOnMainThread()
        for (i in 0..count) adapter.notifyItemMoved(from+i, to+i)
    }

    override fun onItemRangeInserted(list: ObservableList<T>, from: Int, count: Int) {
        val adapter = adapterRef.get() ?: return
        ensureChangeOnMainThread()
        adapter.notifyItemRangeInserted(from, count)
    }

    override fun onItemRangeRemoved(list: ObservableList<T>, from: Int, count: Int) {
        val adapter = adapterRef.get() ?: return
        ensureChangeOnMainThread()
        adapter.notifyItemRangeRemoved(from, count)
    }

    private fun ensureChangeOnMainThread() {
        val bg = Thread.currentThread() != Looper.getMainLooper().thread
        if (bg) throw IllegalStateException("You cannot modify the ObservableList on a background thread")
    }

}
