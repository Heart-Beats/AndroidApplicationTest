package com.hl.utils

import androidx.recyclerview.widget.RecyclerView

/**
 * @Author  张磊  on  2021/01/14 at 16:41
 * Email: 913305160@qq.com
 */
fun RecyclerView.setItemDecoration(decor: RecyclerView.ItemDecoration) {
    for (i in this.itemDecorationCount - 1 downTo 0) {
        this.removeItemDecoration(this.getItemDecorationAt(i))
    }
    this.addItemDecoration(decor)
}