package com.example.zhanglei.myapplication


import android.graphics.Color
import android.os.Build
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
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hl.utils.StatusBarUtil
import com.hl.utils.navigation.MyNavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.firstChild

@AndroidEntryPoint
class MainActivity : ViewBindingBaseActivity<ActivityMainBinding>() {


    private fun getFragmentById(@IdRes id: Int): Fragment? {
        return supportFragmentManager.findFragmentById(id)
    }

    override fun createViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun ActivityMainBinding.onViewCreated(savedInstanceState: Bundle?) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //注意要清除 FLAG_TRANSLUCENT_STATUS flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.TRANSPARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        val myNavHostFragment = getFragmentById(R.id.nav_host_fragment) as MyNavHostFragment
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
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(com.hl.utils.navigation.NavAnimations.NO_ANIM)
            .setExitAnim(com.hl.utils.navigation.NavAnimations.NO_ANIM)
            .setPopEnterAnim(com.hl.utils.navigation.NavAnimations.NO_ANIM)
            .setPopExitAnim(com.hl.utils.navigation.NavAnimations.NO_ANIM).apply {
                if (item.order and Menu.CATEGORY_SECONDARY == 0) {
                    setPopUpTo(findStartDestination(navController.graph)?.id ?: -1, true)
                }
            }
            .build()

        return try {
            navController.navigate(item.itemId, null, navOptions)
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
