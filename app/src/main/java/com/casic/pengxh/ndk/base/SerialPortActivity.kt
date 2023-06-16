package com.casic.pengxh.ndk.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.casic.pengxh.ndk.uart.SerialPort
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidParameterException


abstract class SerialPortActivity : AppCompatActivity() {

    private val kTag = "SerialPortActivity"
    private var serialPort: SerialPort? = null
    var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    private var readThread: ReadThread? = null

    inner class ReadThread : Thread() {
        override fun run() {
            super.run()
            while (!isInterrupted) {
                var size: Int
                try {
                    val buffer = ByteArray(64)
                    if (inputStream == null) return
                    size = inputStream!!.read(buffer)
                    if (size > 0) {
                        onDataReceived(buffer, size)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    return
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initLayoutView())
        setupTopBarLayout()
        initData(savedInstanceState)
        observeRequestState()
        initEvent()

        try {
            serialPort = BaseApplication.get().getSerialPort()
            outputStream = serialPort?.outputStream
            inputStream = serialPort?.inputStream

            readThread = ReadThread()
            readThread?.start()
            Toast.makeText(this, "串口已打开！", Toast.LENGTH_SHORT).show()
            Log.d(kTag, "onCreate => 串口已打开！")
        } catch (e: SecurityException) {
            Toast.makeText(this, "您没有串口的读写权限！", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Toast.makeText(this, "因为不明原因，串口无法打开！", Toast.LENGTH_SHORT).show()
        } catch (e: InvalidParameterException) {
            Toast.makeText(this, "请检查串口！", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 初始化xml布局
     */
    abstract fun initLayoutView(): Int

    /**
     * 特定页面定制沉浸式状态栏
     */
    abstract fun setupTopBarLayout()

    /**
     * 初始化默认数据
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * 数据请求状态监听
     */
    abstract fun observeRequestState()

    /**
     * 初始化业务逻辑
     */
    abstract fun initEvent()

    /**
     * 串口读数
     * */
    abstract fun onDataReceived(buffer: ByteArray?, size: Int)

    override fun onDestroy() {
        readThread?.interrupt()
        BaseApplication.get().closeSerialPort()
        serialPort = null
        super.onDestroy()
    }
}