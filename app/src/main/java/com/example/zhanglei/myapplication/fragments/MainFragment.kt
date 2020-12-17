package com.example.zhanglei.myapplication.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.zhanglei.myapplication.*
import com.example.zhanglei.myapplication.MyApplication.Companion.getInstance
import com.hl.downloader.DownloadListener
import com.hl.downloader.DownloadManager.cancelDownload
import com.hl.downloader.DownloadManager.pauseDownload
import com.hl.downloader.DownloadManager.resumeDownLoad
import com.hl.downloader.DownloadManager.startDownLoad
import kotlinx.android.synthetic.main.fragment_main.*
import java.lang.Math.PI
import java.util.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

/**
 * @author 张磊
 */
class MainFragment : BaseFragment(), View.OnClickListener {
    private var myView: MyView? = null
    private var startAnimation: Button? = null
    private var pictureSelect: Button? = null
    private var myAnimatorSet: MyAnimatorSet? = null
    private var playVideo: Button? = null
    private var download: Button? = null

    override val layoutResId: Int
        get() = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (toolbar != null) {
            toolbar?.title = "主页"
        }
        myView = view.findViewById(R.id.my_view)
        startAnimation = view.findViewById(R.id.start_animation)
        pictureSelect = view.findViewById(R.id.picture_select)
        playVideo = view.findViewById(R.id.play_video)
        myAnimatorSet = MyAnimatorSet()
        download = view.findViewById(R.id.download)
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()
        connectivityManager.registerNetworkCallback(request, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                println("wifi可用")
                resumeDownLoad()
            }

            override fun onLost(network: Network) {
                println("wifi断开连接")
                pauseDownload()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        myView?.setOnClickListener(object : ClickHelperListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onDoubleClick(v: View) {
                myView?.fixedColor()
                myAnimatorSet?.pause()
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            override fun onSingleClick(v: View) {
                myView?.changeColor()
                myAnimatorSet?.resume()
            }
        })
        startAnimation?.setOnClickListener(this)
        pictureSelect?.setOnClickListener(this)
        playVideo?.setOnClickListener(this)
        download?.setOnClickListener(this)

        goto_notify_fragment?.setOnClickListener(this)

        download?.setOnLongClickListener {
            cancelDownload()
            true
        }
    }

    private fun setScaleAnimation() {
        Log.d(TAG, "setScaleAnimation: ")
        val animatorX = ObjectAnimator()
        animatorX.setPropertyName("scaleX")
        animatorX.setFloatValues(0.1f)
        animatorX.duration = 5000
        animatorX.target = myView
        val animatorY = ObjectAnimator()
        animatorY.setPropertyName("scaleY")
        animatorY.setFloatValues(0.1f)
        animatorY.duration = 5000
        animatorY.target = myView
        myAnimatorSet?.setPlayWithAnimator(animatorX, animatorY)
    }

    private fun setRotateAnimation() {
        Log.d(TAG, "setRotateAnimation: ")
        val animator = ObjectAnimator()
        animator.setPropertyName("rotation")
        animator.setFloatValues(360f)
        animator.repeatCount = -1
        animator.interpolator = LinearInterpolator()
        animator.duration = 2000
        animator.target = myView
        myAnimatorSet?.setAfterAnimator(animator)
        val animator2 = ObjectAnimator()
        animator2.setPropertyName("translationX")
        animator2.interpolator = LinearInterpolator()
        animator2.duration = 500
        animator2.target = myView
        val animator3 = ObjectAnimator()
        animator3.setPropertyName("translationX")
        animator3.interpolator = LinearInterpolator()
        animator3.duration = 500
        animator3.target = myView
        val startPoint = Point(0f, 0f)
        val endPoint = Point(200f, 200f)
        val animator1 = ValueAnimator.ofObject(PointEvaluator(), startPoint, endPoint)
        animator1.duration = 5000
        animator1.addUpdateListener { animation: ValueAnimator ->
            val point = animation.animatedValue as Point
            Log.d(TAG, "onAnimationUpdate: " + point.x + "," + point.y)
            myView?.x = point.x
            myView?.y = point.y
            myView?.invalidate()
        }
        animator1.start()
        var t = 0
        while (t <= 2 * PI) {
            try {
                Thread.sleep(50)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            t += 1
        }
    }

    private fun requestPermission() {
        val requestPermissionsList: MutableList<String> = ArrayList()
        for (permission in permissionsList) {
            if (ContextCompat.checkSelfPermission(requireContext(),
                            permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsList.add(permission)
            }
        }
        if (requestPermissionsList.isNotEmpty()) {
            val requestPermissions = requestPermissionsList.toTypedArray()
            ActivityCompat.requestPermissions(requireActivity(), requestPermissions, REQUEST_CODE)
        } else {
            startPictureSelectionActivity()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(requireContext(), "必须同意所有权限才能使用本功能",
                                Toast.LENGTH_SHORT).show()
                        return
                    }
                }
                startPictureSelectionActivity()
            }
        }
    }

    private fun startPictureSelectionActivity() {
        val intent = Intent()
        intent.setClass(requireContext(), PictureSelectionActivity::class.java)
        startActivity(intent)
    }

    data class Point(val x: Float, val y: Float)

    class PointEvaluator : TypeEvaluator<Point> {
        private var t = 0.0
        override fun evaluate(fraction: Float, startValue: Point, endValue: Point): Point {
            val x = 16 * sin(t / PI).pow(3.0)
            val y = 13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)
            val point = Point(x.toFloat(), y.toFloat())
            t += 0.1
            return point
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private var permissionsList = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE
        )
        private const val REQUEST_CODE = 1
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.start_animation -> {
                setScaleAnimation()
                setRotateAnimation()
                myAnimatorSet?.start()
            }
            R.id.picture_select -> requestPermission()
            R.id.play_video -> findNavController().navigate(R.id.action_mainFragment_to_videoFragment, null, null)
            R.id.download -> {
                val downloadUrl = "http://down.qq.com/qqweb/QQ_1/android_apk/Androidqq_8.4.10.4875_537065980.apk"
                // String downloadUrl = "https://images.pexels.com/photos/4993088/pexels-photo-4993088.jpeg?cs=srgb&dl=pexels-rachel-claire-4993088.jpg&fm=jpg";
                startDownLoad(getInstance(), downloadUrl, object : DownloadListener() {
                    override fun downloadIng(progress: String) {
                        println(progress)
                    }
                }, 3)
            }
            R.id.goto_notify_fragment -> findNavController().navigate(R.id.action_mainFragment_to_notifyFragment)
            else -> {
            }
        }
    }
}