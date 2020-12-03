package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.zhanglei.myapplication.R
import kotlinx.android.synthetic.main.fragment_first.*

class FirstFragment : BaseFragment() {

    override val layoutResId: Int
        get() = R.layout.fragment_first


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbar?.title = "第一页"

        motionlayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                //TODO("Not yet implemented")
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                button.text = "动画开始"
            }

            override fun onTransitionChange(motionLayout: MotionLayout, p1: Int, p2: Int, p3: Float) {
//               Toast.makeText(requireContext(), "动画执行中", Toast.LENGTH_SHORT).show()
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                button.text = "动画结束"
            }
        })
    }
}