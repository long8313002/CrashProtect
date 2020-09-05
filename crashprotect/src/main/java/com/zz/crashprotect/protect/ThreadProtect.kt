package com.zz.crashprotect.protect

import android.app.Application
import android.os.Looper
import com.zz.crashprotect.CrashException
import com.zz.crashprotect.IProtect

/**
 * 对子线程的异常采取忽略策略
 */
class ThreadProtect : IProtect {

    override fun protect(app: Application) {

        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            UncaughtExceptionHandlerProxy(
                defaultExceptionHandler
            )
        )
    }

    class UncaughtExceptionHandlerProxy(private val base: Thread.UncaughtExceptionHandler?) :
        Thread.UncaughtExceptionHandler {

        override fun uncaughtException(thread: Thread, p1: Throwable) {
            if (thread == Looper.getMainLooper().thread
                || CrashException(p1).isSystemException()
            ) {//主线程或者系统异常
                base?.uncaughtException(thread, p1)
            }
        }

    }

}