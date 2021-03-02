package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.zhanglei.myapplication.databinding.FragmentFirstBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment

class FirstFragment : ViewBindingBaseFragment<FragmentFirstBinding>() {

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentFirstBinding {
        return FragmentFirstBinding.inflate(inflater, container, false)
    }

    override fun FragmentFirstBinding.onViewCreated(savedInstanceState: Bundle?) {
        toolbar?.title = "第一页"
        button.setOnClickListener {
            if (motionLayout.currentState == motionLayout.startState) {
                motionLayout.transitionToEnd()
            } else {
                motionLayout.transitionToStart()
            }
        }

        motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
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