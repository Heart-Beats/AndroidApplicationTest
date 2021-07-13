package com.example.zhanglei.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.zhanglei.myapplication.databinding.FragmentNotifyBinding
import com.example.zhanglei.myapplication.fragments.base.ViewBindingBaseFragment

class NotifyFragment : ViewBindingBaseFragment<FragmentNotifyBinding>() {


    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentNotifyBinding {
        return FragmentNotifyBinding.inflate(inflater, container, false)
    }

    override fun FragmentNotifyBinding.onViewCreated(savedInstanceState: Bundle?) {

    }

}