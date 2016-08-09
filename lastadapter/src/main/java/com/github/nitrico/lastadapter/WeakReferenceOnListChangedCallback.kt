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

import android.databinding.ObservableList
import android.os.Looper
import java.lang.ref.WeakReference

internal class WeakReferenceOnListChangedCallback<T: Any>(adapter: LastAdapter<T>)
: ObservableList.OnListChangedCallback<ObservableList<T>>() {

    private val reference = WeakReference<LastAdapter<T>>(adapter)

    private val adapter: LastAdapter<T>
        get() {
            if (Thread.currentThread() == Looper.getMainLooper().thread) return reference.get()
            else throw IllegalStateException("You must modify the ObservableList on the main thread")
        }

    override fun onChanged(list: ObservableList<T>)
            = adapter.notifyDataSetChanged()

    override fun onItemRangeChanged(list: ObservableList<T>, from: Int, count: Int)
            = adapter.notifyItemRangeChanged(from, count)

    override fun onItemRangeInserted(list: ObservableList<T>, from: Int, count: Int)
            = adapter.notifyItemRangeInserted(from, count)

    override fun onItemRangeRemoved(list: ObservableList<T>, from: Int, count: Int)
            = adapter.notifyItemRangeRemoved(from, count)

    override fun onItemRangeMoved(list: ObservableList<T>, from: Int, to: Int, count: Int)
            = with(adapter) { for (i in 0..count-1) notifyItemMoved(from+i, to+i) }

}
