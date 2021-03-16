package com.example.zhanglei.myapplication


import android.os.Bundle
import android.view.*
import androidx.annotation.IdRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.zhanglei.myapplication.activities.base.ViewBindingBaseActivity
import com.example.zhanglei.myapplication.databinding.ActivityMainBinding
import com.example.zhanglei.myapplication.utils.navigationutil.MyNavHostFragment
import com.example.zhanglei.myapplication.utils.navigationutil.NavAnimations
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.firstChild

class MainActivity : ViewBindingBaseActivity<ActivityMainBinding>() {


    private fun getFragmentFragmentById(@IdRes id: Int): Fragment? {
        return supportFragmentManager.findFragmentById(id)
    }

    override fun createViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun ActivityMainBinding.onViewCreated(savedInstanceState: Bundle?) {
        val myNavHostFragment = getFragmentFragmentById(R.id.nav_host_fragment) as MyNavHostFragment
        myNavHostFragment.run {
            this.setCommonNavAnimations {
                this.enterAnim = R.anim.slide_in_right
                this.exitAnim = R.anim.slide_out_left
                this.popEnterAnim = R.anim.slide_in_left
                this.popExitAnim = R.anim.slide_out_right
            }
            this.setSpecialDeepLinks(deepLinksRes = listOf(
                    R.string.global_goto_uni_app_page
            ))
        }

        val findNavController = myNavHostFragment.findNavController().apply {
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

    private fun BottomNavigationView.setupWithNavController(navController: NavController) {
        setOnNavigationItemSelectedListener { item ->
            if (!item.isChecked) {
                onNavDestinationSelected(item, navController)
            } else {
                true
            }
        }
    }

    private fun onNavDestinationSelected(item: MenuItem, navController: NavController): Boolean {
        val builder = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(NavAnimations.NO_ANIM)
                .setExitAnim(NavAnimations.NO_ANIM)
                .setPopEnterAnim(NavAnimations.NO_ANIM)
                .setPopExitAnim(NavAnimations.NO_ANIM)
        if (item.order and Menu.CATEGORY_SECONDARY == 0) {
            builder.setPopUpTo(findStartDestination(navController.graph)?.id ?: -1, true)
        }
        return try {
            navController.navigate(item.itemId, null, builder.build())
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun findStartDestination(graph: NavGraph): NavDestination? {
        var startDestination: NavDestination? = graph
        while (startDestination is NavGraph) {
            val parent = startDestination
            startDestination = parent.findNode(parent.startDestination)
        }
        return startDestination
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
