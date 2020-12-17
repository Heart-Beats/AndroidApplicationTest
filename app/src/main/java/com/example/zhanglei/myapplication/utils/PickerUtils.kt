package com.example.zhanglei.myapplication.utils

import android.widget.CheckedTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.example.zhanglei.myapplication.R
import java.util.*

/**
 * @Author  张磊  on  2020/12/16 at 19:55
 * Email: 913305160@qq.com
 */

fun Fragment.initTimePickerView(initStartTime: String?, initEndTime: String?) {
    val pattern = "HH:mm"
    val startDate = initStartTime?.toCalendar(pattern) ?: Calendar.getInstance()
    val endDate = initEndTime?.toCalendar(pattern) ?: Calendar.getInstance()



    var cancel: TextView? = null
    var sure: TextView? = null

    var startTime: CheckedTextView? = null
    var endTime: CheckedTextView? = null
    val timePickerView = TimePickerBuilder(requireContext()) { _, _ ->
        //点击完成按钮回调


    }.setType(booleanArrayOf(false, false, false, true, true, false)) //分别对应年月日时分秒，默认全部显示
            .setContentTextSize(18) //滚轮文字大小
            .setOutSideCancelable(false) //点击屏幕，点在控件外部范围时，是否取消显示
            .isCyclic(true) //是否循环滚动
            // .setBgColor(Color.WHITE) //滚轮背景颜色
            .setDate(startDate) // 如果不设置的话，默认是系统时间
            // .setRangDate(startDate, endDate) //起始终止年月日设定
            .setLabel("年", "月", "日", "时", "分", "秒")
            .setLayoutRes(R.layout.pickerview_custom_time) {
                cancel = it.findViewById(R.id.btnCancel)
                sure = it.findViewById(R.id.btnSubmit)
                startTime = it.findViewById(R.id.start_time)
                endTime = it.findViewById(R.id.end_time)

                startTime?.text = initStartTime
                endTime?.text = initEndTime
            }
            .setTimeSelectChangeListener {
                if (startTime?.isChecked == true) {
                    startDate.time = it
                    startTime?.text = it.toFormatString(pattern)
                }
                if (endTime?.isChecked == true) {
                    endDate.time = it
                    endTime?.text = it.toFormatString(pattern)
                }
            }
            .build()

    cancel?.setOnClickListener(timePickerView)
    sure?.setOnClickListener(timePickerView)

    startTime?.setOnClickListener {
        timePickerView.setDate(startDate)
        startTime?.isChecked = true
        endTime?.isChecked = false
    }
    endTime?.setOnClickListener {
        timePickerView.setDate(endDate)
        endTime?.isChecked = true
        startTime?.isChecked = false
    }

    timePickerView.show()
}
