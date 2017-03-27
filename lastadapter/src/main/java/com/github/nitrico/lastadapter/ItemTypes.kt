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

import android.databinding.ViewDataBinding

open class SimpleType(internal val layout: Int)

@Suppress("unused")
open class BaseType<B : ViewDataBinding>(layout: Int) : SimpleType(layout)

open class ItemType<B : ViewDataBinding>(layout: Int) : BaseType<B>(layout) {
    open fun onBind(viewHolder: ViewHolder<B>) { }
    open fun onRecycle(viewHolder: ViewHolder<B>) { }
}

open class Type<B : ViewDataBinding>(layout: Int) : BaseType<B>(layout) {
    internal var onBind: ((ViewHolder<B>) -> Unit)? = null; private set
    internal var onClick: ((ViewHolder<B>) -> Unit)? = null; private set
    internal var onLongClick: ((ViewHolder<B>) -> Unit)? = null; private set
    internal var onRecycle: ((ViewHolder<B>) -> Unit)? = null; private set

    fun onBind(f: (ViewHolder<B>) -> Unit) = apply { onBind = f }
    fun onClick(f: (ViewHolder<B>) -> Unit) = apply { onClick = f }
    fun onLongClick(f: (ViewHolder<B>) -> Unit) = apply { onLongClick = f }
    fun onRecycle(f: (ViewHolder<B>) -> Unit) = apply { onRecycle = f }
}
