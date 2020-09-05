package com.zz.crashprotect.protect.activitystartup

import android.app.Activity
import android.app.Instrumentation
import android.os.Bundle
import android.os.PersistableBundle
import com.zz.crashprotect.ReflexUtil

/**
 * 代理类，对activity的启动进行保护
 */
internal class InstrumentationProxy(base: Instrumentation) : Instrumentation() {

    init {
        ReflexUtil.copyTo(base, this)
    }


    private fun onException(e: Exception, activity: Activity?) {
        e.printStackTrace()
        activity?.finish()
    }

    override fun callActivityOnCreate(activity: Activity?, icicle: Bundle?) {
        try {
            super.callActivityOnCreate(activity, icicle)
        } catch (e: Exception) {
            onException(e, activity)
        }
    }

    override fun callActivityOnCreate(
        activity: Activity?,
        icicle: Bundle?,
        persistentState: PersistableBundle?
    ) {
        try {
            super.callActivityOnCreate(activity, icicle, persistentState)
        } catch (e: Exception) {
            onException(e, activity)
        }

    }

    override fun callActivityOnResume(activity: Activity?) {
        try {
            super.callActivityOnResume(activity)
        } catch (e: Exception) {
            onException(e, activity)
        }

    }

    override fun callActivityOnPause(activity: Activity?) {
        try {
            super.callActivityOnPause(activity)
        } catch (e: Exception) {
            onException(e, activity)
        }
    }

    override fun callActivityOnDestroy(activity: Activity?) {
        try {
            super.callActivityOnDestroy(activity)
        } catch (e: Exception) {
            onException(e, activity)
        }
    }

    override fun callActivityOnStop(activity: Activity?) {
        try {
            super.callActivityOnStop(activity)
        } catch (e: Exception) {
            onException(e, activity)
        }
    }

    override fun callActivityOnStart(activity: Activity?) {
        try {
            super.callActivityOnStart(activity)
        } catch (e: Exception) {
            onException(e, activity)
        }
    }
}