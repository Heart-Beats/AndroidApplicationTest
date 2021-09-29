package com.hl.uikit.search

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.*
import com.hl.uikit.R
import kotlin.reflect.KProperty1

/**
 * @Author  张磊  on  2020/10/13 at 9:52
 * Email: 913305160@qq.com
 */
class UIKitSearchBar : SearchView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.uikit_searchBarStyle
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private val uiScope = MainScope()

    private var isOnlyVisibleSearch = true

    fun <T> initSearchBar(
        queryData: List<T>,
        queryProperty: List<KProperty1<T, String?>?>? = null,
        queryCompleteListener: (List<T>) -> Unit = {}
    ) {
        setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryFilterData(queryData, newText, queryProperty, queryCompleteListener)
                return true
            }
        })
    }

    private fun <T> queryFilterData(
        queryData: List<T>,
        keyword: String,
        queryProperty: List<KProperty1<T, String?>?>? = null,
        queryCompleteListener: (List<T>) -> Unit = {}
    ) {
        uiScope.launch {
            val filterData = withContext(Dispatchers.Default) {
                queryData.filter {
                    var list= mutableListOf<Boolean>()
                    queryProperty?.forEach{property->
                        list.add( property?.get(it)?.contains(keyword, true)
                            ?: when {
                                it is String -> {
                                    it.contains(keyword, true)
                                }
                                it !is String && queryProperty == null -> {
                                    throw Exception("搜索的数据非字符串集合，需要传入查询属性")
                                }
                                else -> {
                                    false
                                }
                            })
                    }
                    var filter=false
                    if(queryProperty.isNullOrEmpty()){
                        filter  =  when {
                            it is String -> {
                                it.contains(keyword, true)
                            }
                            it !is String && queryProperty == null -> {
                                throw Exception("搜索的数据非字符串集合，需要传入查询属性")
                            }
                            else -> {
                                false
                            }
                        }
                    }


                    list.forEach { item->
                        filter=filter||item
                    }
                    filter
//                    queryProperty?.get(0)?.get(it)?.contains(keyword, true)
//                            ?: when {
//                        it is String -> {
//                            it.contains(keyword, true)
//                        }
//                        it !is String && queryProperty == null -> {
//                            throw Exception("搜索的数据非字符串集合，需要传入查询属性")
//                        }
//                        else -> {
//                            false
//                        }
//                    } || queryProperty?.get(1)?.get(it)?.contains(keyword, true)
//                            ?: when {
//                        it is String -> {
//                            it.contains(keyword, true)
//                        }
//                        it !is String && queryProperty == null -> {
//                            throw Exception("搜索的数据非字符串集合，需要传入查询属性")
//                        }
//                        else -> {
//                            false
//                        }
//                    }
                }
            }
            queryCompleteListener(filterData)
        }
    }

    fun setOnlyVisibleSearch(isOnlyVisibleSearch: Boolean) {
        this.isOnlyVisibleSearch = isOnlyVisibleSearch
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        // 当不可见时取消搜索数据
        if (changedView == this && visibility != VISIBLE && isOnlyVisibleSearch) {
            uiScope.cancel()
        }
    }
}