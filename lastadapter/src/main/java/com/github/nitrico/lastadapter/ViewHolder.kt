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

class ViewHolder(internal val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(variable: Int, item: Any) {
        binding.setVariable(variable, item)
        binding.executePendingBindings()
    }

    fun <B : ViewDataBinding> bind(variable: Int, item: Any, type: BaseType<B>) {
        bind(variable, item)
        @Suppress("UNCHECKED_CAST")
        when (type) {
            is Type -> {
                val onClick = type.onClick
                if (onClick != null) itemView.setOnClickListener {
                    onClick(Type.Params(binding as B, adapterPosition, viewHolder = this))
                }
                val onLongClick = type.onLongClick
                if (onLongClick != null) itemView.setOnLongClickListener {
                    onLongClick(Type.Params(binding as B, adapterPosition, viewHolder = this))
                    true
                }
                type.onBind?.invoke(Type.Params(binding as B, adapterPosition, viewHolder = this))
            }
            is ItemType -> type.onBind(binding as B, itemView, adapterPosition)
        }
    }

    fun <B : ViewDataBinding> recycle(type: BaseType<B>) {
        @Suppress("UNCHECKED_CAST")
        when (type) {
            is Type -> type.onRecycle?.invoke(Type.Params(binding as B, adapterPosition, viewHolder = this))
            is ItemType -> type.onRecycle(binding as B, itemView, adapterPosition)
        }
    }

}
