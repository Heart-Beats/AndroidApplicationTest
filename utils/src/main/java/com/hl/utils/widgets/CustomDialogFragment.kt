package com.hl.utils.widgets

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.hl.utils.R

/**
 * @Author  张磊  on  2020/05/25 at 12:59
 * Email: 913305160@qq.com
 */
class CustomDialogFragment : DialogFragment() {

    private val logTag: String? = this::class.java.simpleName

    var resStyleTheme: Int? = 0
    var resLayout: Int? = 0
    var cancelTouchOut: Boolean? = false

    private var inflateView: View? = null
    private var listenerMap: HashMap<Int, (v: View) -> Unit>? = hashMapOf()
    private var textMap: HashMap<Int, CharSequence>? = hashMapOf()
    private var imageUrlMap: HashMap<Int, Any>? = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = cancelTouchOut ?: false
        setStyle(STYLE_NORMAL, resStyleTheme ?: R.style.custom_dialog_fragment)
        Log.d(logTag, "onCreate --- isCancelable == $isCancelable, resStyleTheme == $resStyleTheme")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inflateView = super.onCreateView(inflater, container, savedInstanceState)
        if (inflateView == null) {
            inflateView = resLayout?.let { inflater.inflate(it, container, false) }
        }
        Log.d(logTag, "onCreateView --- resLayout == $resLayout, inflateView == $inflateView")
        return inflateView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(logTag, "onStart")
        listenerMap?.forEach {
            inflateView?.findViewById<View>(it.key)?.setOnClickListener(
                    it.value
            )
        }

        textMap?.forEach {
            val findView = inflateView?.findViewById<View>(it.key)
            if (findView is TextView) {
                findView.text = it.value
            } else {
                throw Exception("$findView is not TextView, can't set text")
            }
        }

        imageUrlMap?.forEach {
            val findView = inflateView?.findViewById<View>(it.key)
            if (findView is ImageView) {
                Glide.with(requireContext()).asBitmap().load(it.value).into(findView)
            } else {
                throw Exception("$findView is not ImageView, can't set image")
            }
        }
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy: ")
        resStyleTheme = null
        resLayout = null
        cancelTouchOut = null
        inflateView = null
        listenerMap = null
        textMap = null
        imageUrlMap = null
        super.onDestroy()
    }

    fun addViewOnclick(viewId: Int, listener: (v: View) -> Unit): CustomDialogFragment {
        listenerMap?.set(viewId, listener)
        return this
    }

    fun removeViewOnclick(viewId: Int): CustomDialogFragment {
        if (listenerMap?.containsKey(viewId) == true) {
            listenerMap?.remove(viewId)
        }
        return this
    }

    fun setViewText(viewId: Int, text: CharSequence): CustomDialogFragment {
        textMap?.set(viewId, text)
        return this
    }

    fun setViewImage(viewId: Int, imageUrl: Any) {
        imageUrlMap?.set(viewId, imageUrl)
    }


}