package com.zz.crashprotect.protect.appprotect

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.lang.Exception
import java.util.*

 object ActivityStackManager {

    private val activityList = LinkedList<Activity>()

    fun init(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                activityList.addFirst(p0)
            }

            override fun onActivityPaused(p0: Activity) {
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityDestroyed(p0: Activity) {
                activityList.remove(p0)
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityStopped(p0: Activity) {
            }


            override fun onActivityResumed(p0: Activity) {
            }
        })
    }

    /**
     * 根据异常找到发生异常的Activity
     * 默认比较栈顶Activity
     * @return 没有找到返回空
     */
    fun exceptionBirthplaceActivity(e: Exception): Activity? {
        if (activityList.isNullOrEmpty()) return null

        val className = activityList.first.javaClass.name
        if (e.stackTrace.first().className.contains(className)) {
            return activityList.first
        }
        return null
    }

}