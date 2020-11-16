package com.example.zhanglei.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author  张磊  on  2020/11/16 at 12:33
 * Email: 913305160@qq.com
 */
abstract class BaseAbstractAdapter<T>(var data: List<T>) : RecyclerView.Adapter<BaseAbstractAdapter<T>.ViewHolder>() {

    abstract val itemLayout: Int

    var onViewHolderInitListener: (itemView: View, position: Int, data: T) -> Unit = { _, _, _ -> }
    var onBindItemListener: (itemView: View, data: T) -> Unit = { _, _ -> }

    inner class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        init {
            onViewHolderInitListener(itemView, viewType, data[viewType])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return ViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindItemListener(holder.itemView, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<T>) {
        this.data = newData
        notifyDataSetChanged()
    }
}