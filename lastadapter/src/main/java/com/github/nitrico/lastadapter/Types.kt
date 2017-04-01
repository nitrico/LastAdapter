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

open class BaseType
@JvmOverloads constructor(open val layout: Int, open val variable: Int? = null)

@Suppress("unused")
abstract class AbsType<B : ViewDataBinding>
@JvmOverloads constructor(layout: Int, variable: Int? = null) : BaseType(layout, variable)

open class ItemType<B : ViewDataBinding>
@JvmOverloads constructor(layout: Int, variable: Int? = null) : AbsType<B>(layout, variable) {
    open fun onCreate(holder: Holder<B>) { }
    open fun onBind(holder: Holder<B>) { }
    open fun onRecycle(holder: Holder<B>) { }
}

open class Type<B : ViewDataBinding>
@JvmOverloads constructor(layout: Int, variable: Int? = null) : AbsType<B>(layout, variable) {
    internal var onCreate: Action<B>? = null; private set
    internal var onBind: Action<B>? = null; private set
    internal var onClick: Action<B>? = null; private set
    internal var onLongClick: Action<B>? = null; private set
    internal var onRecycle: Action<B>? = null; private set
    fun onCreate(action: Action<B>?) = apply { onCreate = action }
    fun onBind(action: Action<B>?) = apply { onBind = action }
    fun onClick(action: Action<B>?) = apply { onClick = action }
    fun onLongClick(action: Action<B>?) = apply { onLongClick = action }
    fun onRecycle(action: Action<B>?) = apply { onRecycle = action }
}

typealias Action<B> = (Holder<B>) -> Unit
