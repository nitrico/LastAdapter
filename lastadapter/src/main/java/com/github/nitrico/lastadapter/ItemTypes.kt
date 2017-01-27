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
import android.support.annotation.IntegerRes
import android.view.View

open class SimpleType(val layout: Int)

@Suppress("unused")
open class BaseType<B : ViewDataBinding>(layout: Int, @IntegerRes vararg val variables: Int) : SimpleType(layout)

open class ItemType<B : ViewDataBinding>(layout: Int, @IntegerRes vararg variables: Int) : BaseType<B>(layout, *variables) {
    open fun onBind(binding: B, view: View, position: Int) { }
    open fun onRecycle(binding: B, view: View, position: Int) { }
}

open class Type<B : ViewDataBinding>(layout: Int) : BaseType<B>(layout) {

    class Params<out B : ViewDataBinding>(val binding: B, val position: Int,
                                          val view: View = binding.root,
                                          val viewHolder: ViewHolder? = null)

    internal var onBind: (Params<B>.() -> Unit)? = null; private set
    internal var onClick: (Params<B>.() -> Unit)? = null; private set
    internal var onLongClick: (Params<B>.() -> Unit)? = null; private set
    internal var onRecycle: (Params<B>.() -> Unit)? = null; private set

    fun onBind(f: Params<B>.() -> Unit) = apply { onBind = f }
    fun onClick(f: Params<B>.() -> Unit) = apply { onClick = f }
    fun onLongClick(f: Params<B>.() -> Unit) = apply { onLongClick = f }
    fun onRecycle(f: Params<B>.() -> Unit) = apply { onRecycle = f }

}
