package com.example.zhanglei.myapplication

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zhanglei.myapplication.utils.MyNavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.firstChild

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myNavHostFragment = nav_host_fragment as MyNavHostFragment
        myNavHostFragment.setCommonNavAnimations {
            this.enterAnim = R.anim.slide_in_right
            this.exitAnim = R.anim.slide_out_left
            this.popEnterAnim = R.anim.slide_in_left
            this.popExitAnim = R.anim.slide_out_right
        }

        val findNavController = findNavController(R.id.nav_host_fragment).apply {
            this.addOnDestinationChangedListener { _, destination, _ ->
                val itemIds = bottomNavigationView.menu.children.toList().map {
                    it.itemId
                }
                when (destination.id) {
                    in itemIds -> {
                        bottomNavigationView.visibility = View.VISIBLE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.INVISIBLE
                    }
                }
            }
        }
        bottomNavigationView.run {
            setupWithNavController(findNavController)
            initTouchHandle()
        }
    }

    private fun BottomNavigationView.initTouchHandle() {

        //拦截 bottomNavigationMenuView 的点击与长按处理（消除长按的 toast 提示，且长按处理与点击处理一致）
        val bottomNavigationMenuView = this.firstChild { it is BottomNavigationMenuView } as BottomNavigationMenuView
        bottomNavigationMenuView.children.forEach {
            if (it is BottomNavigationItemView) {
                it.setOnTouchListener { v, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        if (event.x >= 0 && event.x <= v.width && event.y >= 0 && event.y <= v.height) {
                            //仅当触摸点在 view 之内才执行 view 原有的点击事件
                            v.performClick()
                        }
                    }
                    true
                }
            }
        }
    }
}
