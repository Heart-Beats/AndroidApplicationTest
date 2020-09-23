package com.example.zhanglei.myapplication.fragment

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import com.example.zhanglei.myapplication.R
import kotlinx.android.synthetic.main.fragment_first.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FirstFragment : BaseFragment() {
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FirstFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}