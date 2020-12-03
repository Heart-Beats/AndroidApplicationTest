package com.example.zhanglei.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zhanglei.myapplication.fragments.MyNavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myNavHostFragment = nav_host_fragment as MyNavHostFragment

        println(myNavHostFragment)
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
        bottomNavigationView.setupWithNavController(findNavController)
    }
}
