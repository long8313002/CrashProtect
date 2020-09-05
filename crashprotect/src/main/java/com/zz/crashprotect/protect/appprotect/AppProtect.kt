package com.zz.crashprotect.protect.appprotect

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.zz.crashprotect.CrashException
import com.zz.crashprotect.IProtect
import java.lang.Exception

class AppProtect : IProtect {
    override fun protect(app: Application) {
        ActivityStackManager.init(app)
        protectApp(null)
    }

    private fun protectApp(preException: CrashException?) {
        Handler(Looper.getMainLooper()).post {
            try {
                Looper.loop()
            } catch (e: Exception) {
                val currentException = CrashException(e)
                if (currentException.analysis(preException)) {
                    val activity =
                        ActivityStackManager.exceptionBirthplaceActivity(e)//只针对Activity发生的异常进行保护
                    if (activity != null) {
                        protectApp(currentException)
                        activity.finish()
                        return@post
                    }
                }
                throw e
            }
        }
    }
}