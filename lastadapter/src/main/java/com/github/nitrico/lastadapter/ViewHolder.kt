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
import android.support.v7.widget.RecyclerView

class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root) {

    internal fun bind(variable: Int, item: Any) {
        binding.setVariable(variable, item)
        binding.executePendingBindings()
    }

    internal fun bind(variable: Int, item: Any, type: BaseType<B>) {
        bind(variable, item)
        when (type) {
            is Type -> {
                setOnClickListener(type)
                setOnLongClickListener(type)
                type.onBind?.invoke(this)
            }
            is ItemType -> type.onBind(this)
        }
    }

    internal fun recycle(type: BaseType<B>) {
        when (type) {
            is Type -> type.onRecycle?.invoke(this)
            is ItemType -> type.onRecycle(this)
        }
    }

    private fun setOnClickListener(type: Type<B>) {
        val onClick = type.onClick
        if (onClick != null) {
            itemView.setOnClickListener {
                onClick(this)
            }
        }
    }

    private fun setOnLongClickListener(type: Type<B>) {
        val onLongClick = type.onLongClick
        if (onLongClick != null) {
            itemView.setOnLongClickListener {
                onLongClick(this)
                true
            }
        }
    }

}
