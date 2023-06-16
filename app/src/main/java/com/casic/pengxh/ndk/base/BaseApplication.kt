package com.casic.pengxh.ndk.base

import android.app.Application
import com.casic.pengxh.ndk.uart.SerialPort
import java.io.File
import java.io.IOException
import java.security.InvalidParameterException
import kotlin.properties.Delegates


class BaseApplication : Application() {

    private val kTag = "BaseApplication"
    private var serialPort: SerialPort? = null

    @Throws(SecurityException::class, IOException::class, InvalidParameterException::class)
    fun getSerialPort(): SerialPort? {
        if (serialPort == null) {
            /**
             * Open the serial port，以实际情况为准
             * */
            serialPort =
                SerialPort(File("/dev/ttysWK1"), 9600, 0)
        }
        return serialPort
    }

    companion object {
        private var application: BaseApplication by Delegates.notNull()

        fun get() = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    fun closeSerialPort() {
        if (serialPort != null) {
            serialPort?.close()
            serialPort = null
        }
    }
}