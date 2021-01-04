package com.example.zhanglei.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @Author  张磊  on  2020/11/16 at 12:33
 * Email: 913305160@qq.com
 */
abstract class BaseAbstractAdapter<T>(var data: List<T>) : RecyclerView.Adapter<BaseAbstractAdapter<T>.ViewHolder>() {

    var headerView: View? = null
    var footerView: View? = null

    abstract val itemLayout: Int

    var onViewHolderInitListener: (viewHolder: ViewHolder, position: Int, data: T?) -> Unit = { _, _, _ -> }
    var onBindItemListener: (viewHolder: ViewHolder, data: T?) -> Unit = { _, _ -> }

    inner class ViewHolder(itemView: View, viewType: Int) : RecyclerView.ViewHolder(itemView) {

        init {
            val data = getDataForHeaderAndFooter(viewType)
            onViewHolderInitListener(this, viewType, data)
        }

        fun <V : View> getView(viewId: Int): V? {
            return itemView.findViewById(viewId)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        if (viewType == 0) {
            itemView = headerView ?: itemView
        } else if (viewType == itemCount - 1) {
            itemView = footerView ?: itemView
        }
        return ViewHolder(itemView, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getDataForHeaderAndFooter(position)
        onBindItemListener(holder, data)
    }

    /**
     * 获取头尾存在或不存在时对应位置的数据
     */
    private fun getDataForHeaderAndFooter(position: Int): T? {
        //头尾布局都存在时
        return if (headerView != null && footerView != null) {
            if (position == 0 || position == itemCount - 1) {
                //头尾ViewHolder处理
                null
            } else {
                //正常的ViewHolder处理
                data[position - 1]
            }
        } else {
            if (headerView == null && footerView == null) {
                //头尾布局都不存在时的处理
                data[position]
            } else if (headerView != null) {
                //仅头布局存在时
                if (position == 0) {
                    null
                } else {
                    data[position - 1]
                }
            } else if (footerView != null) {
                //仅尾布局存在时
                if (position < itemCount - 1) {
                    data[position]
                } else {
                    null
                }
            } else null
        }
    }

    override fun getItemCount(): Int {
        return data.size + if (headerView == null) 0 else 1 + if (footerView == null) 0 else 1
    }

    fun updateData(newData: List<T>) {
        this.data = newData
        notifyDataSetChanged()
    }
}