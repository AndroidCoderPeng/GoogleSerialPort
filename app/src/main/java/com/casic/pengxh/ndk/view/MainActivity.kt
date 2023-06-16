package com.casic.pengxh.ndk.view

import android.os.Bundle
import com.casic.pengxh.ndk.R
import com.casic.pengxh.ndk.base.SerialPortActivity

class MainActivity : SerialPortActivity() {

    override fun initLayoutView(): Int = R.layout.activity_main

    override fun setupTopBarLayout() {

    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun observeRequestState() {

    }

    override fun initEvent() {

    }

    override fun onDataReceived(buffer: ByteArray?, size: Int) {

    }
}